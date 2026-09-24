use libreriadb_in4cm;
 
drop table if exists detalle_compra;

drop table if exists compras;
 
-- 2. Crear la nueva tabla 

create table if not exists ventas (

    id_venta int primary key auto_increment,

    fecha_venta timestamp default current_timestamp,

    subtotal decimal(8,2) not null default 0.00,

    descuento decimal(10,2) not null default 0.00,

    usuario_autoriza_descuento int,

    total decimal(8,2) not null default 0.00,

    estado varchar(20) default 'COMPLETADA',

    cui_cliente bigint,

    id_usuario int,

    constraint fk_ventas_cliente foreign key (cui_cliente) references clientes(cui) on delete cascade

);

 alter table ventas 
add column motivo_anulacion text null,
add column fecha_anulacion datetime null;
-- 3. Crear la nueva tabla detalle_venta

create table if not exists detalle_venta (

    id_detalle int primary key auto_increment,

    id_venta int,

    isbn varchar(20),

    cantidad int not null,

    precio_unitario decimal(8,2) not null,

    subtotal decimal(8,2) not null,

    constraint fk_detalle_venta foreign key (id_venta) references ventas(id_venta) on delete cascade,

    constraint fk_detalle_libro foreign key (isbn) references libros(isbn) on delete cascade

);
 
-- 4. Eliminar procedimientos almacenados y vistas antiguas de compras

drop procedure if exists sp_insertarcompra;

drop procedure if exists sp_listarcompras;

drop procedure if exists sp_buscarcompra;

drop procedure if exists sp_actualizarcompra;

drop procedure if exists sp_eliminarcompra;

drop procedure if exists sp_insertardetallecompra;

drop procedure if exists sp_listardetallecompra;

drop procedure if exists sp_buscardetallecompra;

drop procedure if exists sp_actualizardetallecompra;

drop procedure if exists sp_eliminardetallecompra;

drop procedure if exists sp_descontarstock;
 
drop view if exists vw_lista_compras;

drop view if exists vw_lista_detalle_compra;

drop view if exists vw_factura_compras;
 
-- 5. Crear Stored Procedures para Ventas y Detalle de Venta

delimiter $$
 
drop procedure if exists sp_insertarventa $$

create procedure sp_insertarventa(

    in _subtotal decimal(8,2),

    in _descuento decimal(8,2),

    in _total decimal(8,2),

    in _cui_cliente bigint,

    in _id_usuario int,

    out _id_venta int

)

begin

    insert into ventas(subtotal, descuento, total, cui_cliente, id_usuario) 

    values (_subtotal, _descuento, _total, _cui_cliente, _id_usuario);

    set _id_venta = last_insert_id();

end $$
 
drop procedure if exists sp_listarventas $$

create procedure sp_listarventas()

begin

    select id_venta, fecha_venta, subtotal, descuento, total, estado, cui_cliente, id_usuario from ventas;

end $$
 
drop procedure if exists sp_buscarventa $$

create procedure sp_buscarventa(in _id_venta int)

begin

    select id_venta, fecha_venta, subtotal, descuento, total, estado, cui_cliente, id_usuario from ventas where id_venta = _id_venta;

end $$
 
drop procedure if exists sp_insertardetalleventa $$

create procedure sp_insertardetalleventa(

    in _id_venta int,

    in _isbn varchar(20),

    in _cantidad int,

    in _precio_unitario decimal(8,2),

    in _subtotal decimal(8,2)

)

begin

    insert into detalle_venta(id_venta, isbn, cantidad, precio_unitario, subtotal) 

    values (_id_venta, _isbn, _cantidad, _precio_unitario, _subtotal);

end $$
 
drop procedure if exists sp_listardetalleventa $$

create procedure sp_listardetalleventa(in _id_venta int)

begin

    select id_detalle, id_venta, isbn, cantidad, precio_unitario, subtotal 

    from detalle_venta 

    where id_venta = _id_venta;

end $$
 
delimiter ;
 
-- 6. Crear Vistas para Ventas

create or replace view vw_lista_ventas as

select v.id_venta as 'no. venta', v.fecha_venta as 'fecha/hora', v.subtotal, v.descuento, v.total, v.estado, v.cui_cliente as 'cui cliente', concat(cl.nombre_cliente, ' ', cl.apellido_cliente) as 'cliente'

from ventas v

left join clientes cl on v.cui_cliente = cl.cui;
 
create or replace view vw_factura_compras as
select v.id_venta as 'numero_factura', 
       v.fecha_venta as 'fecha_emision', 
       cl.cui as 'cui_cliente', 
       concat(cl.nombre_cliente, ' ', cl.apellido_cliente) as 'nombre_cliente', 
       cl.correo_electronico as 'correo_cliente', 
       l.isbn as 'isbn_libro', 
       l.titulo as 'titulo_libro', 
       dv.cantidad, 
       dv.precio_unitario, 
       dv.subtotal as 'subtotal', 
       v.subtotal as 'subtotal_general', 
       v.descuento, 
       v.total as 'gran_total',
       concat(u.nombre, ' ', u.apellido) as 'usuario_atendio'
from ventas v
inner join clientes cl on v.cui_cliente = cl.cui
inner join detalle_venta dv on v.id_venta = dv.id_venta
inner join libros l on dv.isbn = l.isbn
inner join usuarios u on v.id_usuario = u.id_usuario;

-- 2. Crear el procedimiento almacenado de forma separada y correcta
delimiter $$

drop procedure if exists sp_buscar_factura $$
create procedure sp_buscar_factura(in _id_venta int)
begin
    select * from vw_factura_compras where numero_factura = _id_venta;
end $$

delimiter ;
 -- drop procedure if exists sp_buscar_factura;
 
-- drop procedure if exists sp_resumen_dia;

delimiter $$
create procedure sp_resumen_dia()
begin
    select 
        v.id_venta as numero_factura,
        v.fecha_venta as fecha_emision,
        cl.cui as cui_cliente,
        concat(cl.nombre_cliente, ' ', cl.apellido_cliente) as nombre_cliente,
        cl.correo_electronico as correo_cliente,
        l.isbn as isbn_libro,
        l.titulo as titulo_libro,
        dv.cantidad,
        dv.precio_unitario,
        dv.subtotal,
        concat(u.nombre, ' ', u.apellido) as usuario_atendio,
        v.total as gran_total
    from ventas v
    inner join clientes cl on v.cui_cliente = cl.cui
    inner join detalle_venta dv on v.id_venta = dv.id_venta
    inner join libros l on dv.isbn = l.isbn
    inner join usuarios u on v.id_usuario = u.id_usuario
    where date(v.fecha_venta) = curdate();
end $$
delimiter ;
 
 
select v.id_venta as 'numero_factura', v.fecha_venta as 'fecha_emision', cl.cui as 'cui_cliente', concat(cl.nombre_cliente, ' ', cl.apellido_cliente) as 'nombre_cliente', cl.correo_electronico as 'correo_cliente', l.isbn as 'isbn_libro', l.titulo as 'descripcion_libro', dv.cantidad, dv.precio_unitario, dv.subtotal as 'subtotal_item', v.subtotal as 'subtotal_general', v.descuento, v.total as 'gran_total'

from ventas v

inner join clientes cl on v.cui_cliente = cl.cui

inner join detalle_venta dv on v.id_venta = dv.id_venta

inner join libros l on dv.isbn = l.isbn;
 
use libreriadb_in4cm;

drop table if exists usuarios;

create table usuarios (

    id_usuario int auto_increment primary key,

    nombre_usuario varchar(50) not null unique,

    nombre varchar(50) not null,

    apellido varchar(50) not null,

    correo varchar(100) not null unique,

    contrasena varchar(255) not null,

    rol enum('admin', 'empleado', 'cajero') not null,

    activo boolean default true,

    fecha_creacion timestamp default current_timestamp

);

delimiter //

 
drop procedure if exists sp_autenticarusuario //

create procedure sp_autenticarusuario(

    in _nombre_usuario varchar(100),

    in _contrasena varchar(255)

)

begin

    select id_usuario, nombre_usuario, nombre, apellido, correo, rol, activo

    from usuarios

    where nombre_usuario = _nombre_usuario 

      and contrasena = sha2(_contrasena, 256)

      and activo = true;

end //

drop procedure if exists sp_listarusuarios //

create procedure sp_listarusuarios()

begin
    select 
        id_usuario, 
        nombre_usuario, 
        nombre, 
        apellido, 
        correo, 
        rol, 
        activo 
    from usuarios;

end //

drop procedure if exists sp_buscarusuario //

create procedure sp_buscarusuario(

    in _id_usuario int

)

begin

    select id_usuario, nombre_usuario, nombre, apellido, correo, rol, activo

    from usuarios

    where id_usuario = _id_usuario;

end //

drop procedure if exists sp_insertarusuario //

create procedure sp_insertarusuario(

    in _nombre_usuario varchar(50),

    in _nombre varchar(50),

    in _apellido varchar(50),

    in _correo varchar(100),

    in _contrasena varchar(255),

    in _rol varchar(20),

    in _activo boolean

)

begin

    insert into usuarios (nombre_usuario, nombre, apellido, correo, contrasena, rol, activo)

    values (_nombre_usuario, _nombre, _apellido, _correo, sha2(_contrasena, 256), _rol, _activo);

end //
 
drop procedure if exists sp_actualizarusuario //

create procedure sp_actualizarusuario(

    in _id_usuario int,

    in _nombre_usuario varchar(50),

    in _nombre varchar(50),

    in _apellido varchar(50),

    in _correo varchar(100),

    in _rol varchar(20),

    in _activo boolean

)

begin

    update usuarios

    set nombre_usuario = _nombre_usuario,

        nombre = _nombre,

        apellido = _apellido,

        correo = _correo,

        rol = _rol,

        activo = _activo

    where id_usuario = _id_usuario;

end //
 
drop procedure if exists sp_eliminarusuario //

create procedure sp_eliminarusuario(

    in _id_usuario int

)

begin

    delete from usuarios where id_usuario = _id_usuario;

end //

delimiter ;
 
alter table libros 

add column stock_actual int not null default 0,

add column stock_minimo int not null default 0,

add column activo boolean not null default true;
 
create table if not exists proveedores (

    id_proveedor int primary key auto_increment,

    nit_proveedor varchar(20) unique not null,
    nombre_proveedor varchar(100) not null,
    telefono varchar(15),
    correo varchar(100),
    direccion varchar(150),
    activo boolean not null default true

);
alter table libros add column id_proveedor int;
alter table libros add constraint fk_libros_proveedor foreign key (id_proveedor) references proveedores(id_proveedor) on delete set null;

call sp_insertarusuario('admin', 'Admin', 'Sistema', 'admin', 'admin', 'admin', true);
call sp_insertarusuario('cajero', 'Juan', 'Pérez', 'cajero', 'cajero', 'cajero', true);
call sp_insertarusuario('bodega', 'Carlos', 'López', 'empleado', 'empleado', 'empleado', true);

create table if not exists movimientos_inventario (
    id_movimiento int primary key auto_increment,
    isbn varchar(20),
    tipo_movimiento enum('INGRESO', 'VENTA', 'MERMA', 'TRASLADO', 'DEVOLUCION', 'AJUSTE') not null,
    cantidad int not null,
    fecha_movimiento timestamp default current_timestamp,
    id_usuario int not null,
    observacion text,
    constraint fk_movimientos_libro foreign key (isbn) references libros(isbn) on delete cascade,
    constraint fk_movimientos_usuario foreign key (id_usuario) references usuarios(id_usuario) on delete cascade
);

-- Procedimientos almacenados para MOVIMIENTOS_INVENTARIO

delimiter $$

drop procedure if exists sp_insertarmovimiento $$

create procedure sp_insertarmovimiento(
    in _isbn varchar(20),
    in _tipo_movimiento varchar(15),
    in _cantidad int,
    in _id_usuario int,
    in _observacion text
)
begin
    insert into movimientos_inventario(isbn, tipo_movimiento, cantidad, id_usuario, observacion)
    values (_isbn, _tipo_movimiento, _cantidad, _id_usuario, _observacion);
end $$

drop procedure if exists sp_listarmovimientos $$

create procedure sp_listarmovimientos()
begin
    select id_movimiento, isbn, tipo_movimiento, cantidad, fecha_movimiento, id_usuario, observacion
    from movimientos_inventario;
end $$

drop procedure if exists sp_buscarmovimientosporisbn $$

create procedure sp_buscarmovimientosporisbn(in _isbn varchar(20))
begin
    select id_movimiento, isbn, tipo_movimiento, cantidad, fecha_movimiento, id_usuario, observacion
    from movimientos_inventario
    where isbn = _isbn;
end $$
SQL
delimiter $$

drop procedure if exists sp_eliminarmovimovimientos_inventariomiento $$

create procedure sp_eliminarmovimiento(
    in _id_movimiento int
)
begin
    delete from movimientos_inventario 
    where id_movimiento = _id_movimiento;
end $$

delimiter ;
delimiter $$

drop procedure if exists sp_actualizarmovimiento $$

create procedure sp_actualizarmovimiento(
    in _id_movimiento int,
    in _isbn varchar(20),
    in _tipo_movimiento varchar(15),
    in _cantidad int,
    in _id_usuario int,
    in _observacion text
)
begin
    update movimientos_inventario
    set isbn = _isbn,
        tipo_movimiento = _tipo_movimiento,
        cantidad = _cantidad,
        id_usuario = _id_usuario,
        observacion = _observacion
    where id_movimiento = _id_movimiento;
end $$

delimiter ;

SELECT l.isbn, l.titulo, l.fecha_publicacion, l.precio, l.id_autor, l.id_categoria, l.nit_editorial, l.stock FROM libros l
 