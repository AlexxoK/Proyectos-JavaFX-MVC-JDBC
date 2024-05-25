use superDB;

Delimiter $$
Create function fn_CalcularPromocion(proId int) returns decimal(10,2) deterministic
Begin
    Declare resultado int default 0;
    Declare i int default 1;
    Declare fecF date;

    set resultado = 0; 
    
    resultLoop: loop
        Select fechaFinalizacion into fecF from Promociones
			Where promocionId = i and productoId = proId;

        If fecF is not null then
            IF fecF > Date(now()) then
                Set resultado = 1; 
            End If;
        End If;

        Set i = i + 1; 

        If i > (select count(*) from Promociones where productoId = proId) THEN
            Leave resultLoop; 
        End If;
    End Loop resultLoop;

    return resultado;
End$$
Delimiter ;

Delimiter $$
create function fn_totalFactura(facId int) returns decimal(10,2) deterministic
begin
    declare total decimal(10,2) default 0.0;
    declare i int default 1;
    declare precio decimal(10,2);

    totalLoop: loop
        if fn_CalcularPromocion(facId) = 0 then
            if facId = (select facturaId from DetalleFactura DF where detalleFacturaId = i) then
                set precio = (select P.precioVentaUnitario from Productos P where productoId = (select productoId from DetalleFactura where detalleFacturaId = i));
                set total = total + precio + (precio*0.12);
            end if;
        else 
            if facId = (select facturaId from DetalleFactura DF where detalleFacturaId = i) then
                set precio = (select PR.precioPromocion from Promociones PR where productoId = (select productoId from DetalleFactura where detalleFacturaId = i));
                set total = total + precio + (precio*0.12);
            end if;
        end if;

        if i = (select count(*) from DetalleFactura) then
            leave totalLoop;
        end if;

        set i = i + 1;
    end loop totalLoop;

    call sp_asignarTotalFactura(facId, total);

    return total;
end $$
Delimiter ;

Delimiter $$
create function fn_eliminarStock(proId int) returns int deterministic
begin
    declare cantA int default 0;
    declare cantComprada int default 0;

    select cantidadStock into cantComprada from Productos where productoId = proId;
    
    set cantA = cantComprada - 1;
    
    call sp_modificarStock(proId, cantA);
    
    return cantA;
end $$
Delimiter ;

Delimiter $$
create trigger tg_totalFactura
after insert on DetalleFactura
for each row
Begin
    declare tot decimal(10,2);
    declare cantA int;
    
    set tot = fn_totalFactura(new.facturaId);
    set cantA = fn_eliminarStock(new.productoId); 
End$$
Delimiter ;

Delimiter $$
create function fn_totalCompra(comId int) returns decimal (10,2) deterministic
begin
	declare totC decimal (10,2) default 0.0;
    declare i int default 1;
    declare precio decimal (10,2);
    declare cantComprada int default 0;
    declare curCantCompra, curProductoId, curCompraId int;
    
    declare cursorDetalleCompra cursor for
		select DC.cantidadCompra, DC.productoId, DC.compraId from DetalleCompra DC
	;
    
    open cursorDetalleCompra;
    
    totalLoop : loop
    fetch cursorDetalleCompra into curCantCompra, curProductoId, curCompraId;
    
    if comId = curCompraId then
		set precio = (select P.precioCompra from Productos P where P.productoId = curProductoId);
		set cantComprada = curCantCompra;
		set totC = totC + (precio * cantComprada + (cantComprada*precio*0.12));
    end if;
    
    if i = (select count(*) from DetalleCompra) then
		leave totalLoop;
    end if;
    
    set i = i +1;
    end loop totalLoop;
    
    call sp_asignarTotalCompra(comId, totC);
    
    return totC;
end $$
Delimiter ;

Delimiter $$
create function fn_aumentarStock(proId int) returns int deterministic
begin
    declare cantA int default 0;
    declare cantComprada int default 0;
    declare cantidad int default 0;
	
    select cantidadStock into cantidad from Productos where productoId = proId LIMIT 1;
    select cantidadCompra into cantComprada from DetalleCompra where productoId = proId LIMIT 1;
    
    set cantA = cantA + cantComprada + cantidad;
    
    call sp_modificarStockCompra(proId, cantA);
    
    return cantA;
end $$
Delimiter ;

Delimiter $$
create trigger tg_totalCompra
after insert on DetalleCompra
for each row
Begin
    declare totC decimal(10,2);
    declare cantA int;
    
    set totC= fn_totalCompra(new.compraId);
    set cantA = fn_aumentarStock(new.productoId); 
End$$
Delimiter ;