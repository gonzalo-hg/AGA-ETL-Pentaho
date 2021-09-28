/*let xhr = new XMLHttpRequest();
xhr.open('GET', 'http://localhost:8585/alumnos/plan/52/trimestre/20P/sexo/F', true);

xhr.onload = function() {
    if(this.status === 200) {
        let alumnos = JSON.parse(this.responseText);
        console.log(alumnos)
        for(let i in alumnos) {
			console.log(i);
        }
    }
}
xhr.send();*/


window.onload = function() {
	document.onreadystatechange
	var padre = document.getElementById('table-responsive-padre');
	var tablaalumnos = document.createElement("table");
	tablaalumnos.id = "alumnos";
	var tbodyalumnos = document.createElement("tbody");
	var encabezado = document.createElement("thead");
	var filaEncabezado = document.createElement("tr");
	var e1 = document.createElement("th");
	e1.scope = "col";
	var e2 = document.createElement("th");
	e2.scope = "col";
	var e3 = document.createElement("th");
	e3.scope = "col";
	var e4 = document.createElement("th");
	e4.scope = "col";
	var e5 = document.createElement("th");
	e5.scope = "col";
	var e6 = document.createElement("th");
	e6.scope = "col";
	var e7 = document.createElement("th");
	e7.scope = "col";
	var textoE1 = document.createTextNode("Matricula");
	var textoE2 = document.createTextNode("Plan");
	var textoE3 = document.createTextNode("Edad");
	var textoE4 = document.createTextNode("Paterno");
	var textoE5 = document.createTextNode("Materno");
	var textoE6 = document.createTextNode("Nombre");
	var textoE7 = document.createTextNode("Sexo");
	e1.appendChild(textoE1);
	e2.appendChild(textoE2);
	e3.appendChild(textoE3);
	e4.appendChild(textoE4);
	e5.appendChild(textoE5);
	e6.appendChild(textoE6);
	e7.appendChild(textoE7);
	filaEncabezado.appendChild(e1);
	filaEncabezado.appendChild(e2);
	filaEncabezado.appendChild(e3);
	filaEncabezado.appendChild(e4);
	filaEncabezado.appendChild(e5);
	filaEncabezado.appendChild(e6);
	filaEncabezado.appendChild(e7);
	encabezado.appendChild(filaEncabezado);
	
	
	let xhr = new XMLHttpRequest();
	xhr.open('GET', 'http://localhost:8585/alumnos/plan/52/trimestre/20P/sexo/F', true);
	
	xhr.onload = function() {
	    if(this.status === 200) {
	        let alumnos = JSON.parse(this.responseText);
	        console.log(alumnos)
	        for(let i in alumnos) {
				var fila = document.createElement("tr");
	            var celda1 = document.createElement("td");
	            var celda2 = document.createElement("td");
	            var celda3 = document.createElement("td");
	            var celda4 = document.createElement("td");
	            var celda5 = document.createElement("td");
	            var celda6 = document.createElement("td"); 
	            var celda7 = document.createElement("td");
	            var textoCelda1 = document.createTextNode(alumnos[i].mat);
	            var textoCelda2 = document.createTextNode(alumnos[i].pla);
	            var textoCelda3 = document.createTextNode(alumnos[i].edad);
	            var textoCelda4 = document.createTextNode(alumnos[i].pate);
	            var textoCelda5 = document.createTextNode(alumnos[i].mate);
	            var textoCelda6 = document.createTextNode(alumnos[i].nom);
	            var textoCelda7 = document.createTextNode(alumnos[i].sexo);
	            celda1.appendChild(textoCelda1);
	            celda2.appendChild(textoCelda2);
	            celda3.appendChild(textoCelda3);
	            celda4.appendChild(textoCelda4);
	            celda5.appendChild(textoCelda5);
	            celda6.appendChild(textoCelda6);
	            celda7.appendChild(textoCelda7);
	            fila.appendChild(celda1);
	            fila.appendChild(celda2);
	            fila.appendChild(celda3);
	            fila.appendChild(celda4);
	            fila.appendChild(celda5);
	            fila.appendChild(celda6);
	            fila.appendChild(celda7);
	            tbodyalumnos.appendChild(fila);
	        }
	        tablaalumnos.appendChild(encabezado);
	    	tablaalumnos.appendChild(tbodyalumnos);
	    	padre.appendChild(tablaalumnos);
	        
	    }
	}
	xhr.send();
}