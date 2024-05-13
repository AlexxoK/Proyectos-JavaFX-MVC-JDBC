drop database if exists superDB;

create database if not exists superDB;

use superDB;

set global time_zone = '-6:00';

create table Clientes(
	clienteId int not null auto_increment,
    nombre varchar(30) not null,
    apellido varchar(30) not null,
    nit varchar(15) not null,
    telefono varchar(15) not null,
    direccion varchar(200) not null,
    primary key PK_clienteId (clienteId)
);

create table Cargos(
	cargoId int not null auto_increment,
    nombreCargo varchar(30) not null,
    descripcionCargo varchar(100) not null,
    primary key PK_cargoId (cargoId)
);

create table Compras(
	compraId int not null auto_increment,
    fechaCompra date not null,
    totalCompra decimal(10, 2),
    primary key PK_compraId (compraId)
);

create table Distribuidores(
	distribuidorId int not null auto_increment,
    nombreDistribuidor varchar(30) not null,
    telefonoDistribuidor varchar(15) not null,
    nitDistribuidor varchar(15) not null,
	direccionDistribuidor varchar(200) not null,
    web varchar(50),
    primary key PK_distribuidorId (distribuidorId)
);

create table CategoriaProductos(
	categoriaProductoId int not null auto_increment,
    nombreCategoria varchar(30) not null,
    descripcionCategoria varchar(100) not null,
    Primary key PK_categoriaProductoId (categoriaProductoId)
);

create table Empleados(
	empleadoId int not null auto_increment,
    nombreEmpleado varchar(30) not null,
    apellidoEmpleado varchar(30) not null,
    sueldo decimal(10,2) not null,
    horaEntrada time not null,
    horaSalida time not null,
    cargoId int not null,
    encargadoId int,
    primary key PK_empleadoId (empleadoId),
    constraint FK_encargadoId foreign key Empleados (encargadoId)
		references Empleados (empleadoId),
	constraint FK_Empleados_Cargos foreign key Cargos (cargoId)
		references Cargos (cargoId)
);

create table Facturas(
	facturaId int not null auto_increment,
    fecha date not null,
    hora time not null,
    clienteId int not null,
    empleadoId int not null,
    total decimal(10,2),
    primary key PK_facturaId (facturaId),
    constraint FK_Facturas_Clientes foreign key Clientes (clienteId)
	references Clientes (clienteId),
    constraint FK_Facturas_Empleados foreign key Empleados (empleadoId)
		references Empleados (empleadoId)
);

create table TicketSoporte(
	ticketSoporteId int not null auto_increment,
    descripcionTicket varchar (250),
    estatus varchar (30),
    clienteId int not null,
    facturaId int not null,
	primary key PK_ticketSoporteId (ticketSoporteId),
    constraint FK_TicketSoporte_Clientes foreign key TicketSoporte (clienteId)
		references Clientes (clienteId),
	constraint FK_TicketSoporte_Facturas foreign key TicketSoporte (facturaId)
		references Facturas (facturaId)
);

create table Productos(
	productoId int not null auto_increment,
    nombreProducto varchar(50),
    descripcionProducto varchar(100) not null,
    cantidadStock int not null,
    precioVentaUnitario decimal(10, 2) not null,
    precioVentaMayor decimal(10, 2) not null,
    precioCompra decimal(10, 2) not null,
    distribuidorId int not null,
    categoriaProductoId int not null,
    primary key PK_productoId (productoId),
    constraint FK_Productos_Distribuidores foreign key Distribuidores (distribuidorId)
	references Distribuidores (distribuidorId),
    constraint FK_Productos_CategoriaProductos foreign key CategoriaProductos (categoriaProductoId)
		references CategoriaProductos (categoriaProductoId)
);

create table Promociones(
	promocionId int not null auto_increment,
    precioPromocion decimal (10,2),
    descripcionPromocion varchar (200),
    fechaInicio date,
    fechaFinalizacion date,
    productoId int not null,
    primary key PK_promocionId (promocionId),
    constraint FK_Promociones_Productos foreign key Promociones (productoId)
		references Productos (productoId)
);

create table DetalleFactura(
	detalleFacturaId int not null auto_increment,
    facturaId int not null,
    productoId int not null,
    primary key PK_detalleFacturaId (detalleFacturaId),
    constraint FK_DetalleFactura_Facturas foreign key Facturas (facturaId)
		references Facturas(facturaId),
    constraint FK_DetalleFactura_Productos foreign key Productos (productoId)
		references Productos (productoId)
);

create table DetalleCompra(
	detalleCompraId int not null auto_increment,
    cantidadCompra int not null,
    productoId int not null,
    compraId int not null,
    Primary key PK_detalleCompraId (detalleCompraId),
    constraint FK_DetalleCompra_Productos foreign key (productoId)
		references Productos (productoId),
	constraint FK_DetalleCompra_Compras foreign key (compraId)
		references Compras (compraId)
);

insert into Clientes(nombre, apellido, telefono, nit, direccion) values
	('Luis', 'Cuxun', '1234-1234', '17302703-0', 'Ciudad'),
    ('Alejandro', 'Carrillo', '4234-4234', '36987412-0', 'Ciudad'),
    ('Jesus', 'Sis', '1231-1231', '23548691-0', 'Ciudad');
 
insert into Cargos(nombreCargo, descripcionCargo) values
    ('Gerente de Billar', 'Se encarga de mantener todo en orden.');
    
insert into Compras(fechaCompra, totalCompra) values
    ('2024-03-23', '100.00');

insert into Empleados(nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargoId, encargadoId) values
    ('Carlos', 'Orozco', '200.00', '15:00:00', '23:00:00', 1, 1);
    
insert into Facturas(fecha, hora, clienteId, empleadoId, total) values
    ('2024-03-23', '20:00:00', 1, 1, '17.00');
    
insert into TicketSoporte(descripcionTicket, estatus, clienteId, facturaId) values
    ('Error de prueba', 'Recien creado', 1, 1);
    
insert into Distribuidores(nombreDistribuidor, telefonoDistribuidor, nitDistribuidor, direccionDistribuidor, web) values
    ('Jose', '4321-1234', '23548691','Ciudad', 'MercadoLaQuinta');
    
insert into CategoriaProductos(nombreCategoria, descripcionCategoria) values
    ('Electronicos', 'Variedad de instrumentos electronicos.');
    
insert into Productos(nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, distribuidorId, categoriaProductoId) values
    ('Mouse', 'Mouse para computadora.', 15, 50.00, 100.00, 50.00, 1, 1);
    
insert into Promociones(precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacion, productoId) values
	(15.00, 'Promocion de primavera.', '2024-01-01', '2024-01-02', 1);
    
insert into DetalleFactura(facturaId, productoId) values
	(1, 1);
    
insert into DetalleCompra(cantidadCompra, productoId, compraId) values
	(15, 1, 1);