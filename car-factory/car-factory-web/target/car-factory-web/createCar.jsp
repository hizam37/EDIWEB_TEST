<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Create New Car</title>
<script>
       
        async function createCar() {
            const car = {
                engine: {
                    type: document.getElementById("engineType").value,
                    volume: document.getElementById("volume").value,
                    powerKw: document.getElementById("powerKw").value,
                    serialNumber: document.getElementById("engineSerialNumber").value
                },
                transmission: {
                    type: document.getElementById("transmissionType").value,
                    serialNumber: document.getElementById("transmissionSerialNumber").value
                },
                body: {
                    type: document.getElementById("bodyType").value,
                    color: document.getElementById("color").value,
                    doorCount: document.getElementById("doorCount").value,
                    vin: document.getElementById("vin").value
                }
            };
         
            const response = await fetch('/car-factory-web/api/cars/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(car)
            });

            if (response.ok) {
                alert("Car created successfully.");
                document.getElementById("carForm").reset();
            } else {
                const message = await response.text();
                alert("Error creating car: " + message);
            }
        }

        async function getCarById() {
            var id = document.getElementById("id").value;
            const response = await fetch(`/car-factory-web/api/cars/getCarById/` + id, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const carData = await response.json();
                localStorage.setItem('carData', JSON.stringify(carData));
                alert("Redirecting to update car page.");
                window.location.href = '/car-factory-web/displayCarComponents.jsp';
            } else {
                const message = await response.text();
                alert("Error fetching car: " + message);
            }
        }

        async function getAllCars() {
            const response = await fetch('/car-factory-web/api/cars/getCars', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const cars = await response.json();
                displayCars(cars);
            } else {
                const message = await response.text();
                alert("Error fetching cars: " + message);
            }
        }

        function displayCars(cars) {
            const carsListDiv = document.getElementById("carsList");
            carsListDiv.innerHTML = ""; 

            if (cars.length === 0) {
                carsListDiv.innerHTML = "<p>No cars found.</p>";
                return;
            }

            const table = document.createElement("table");
            table.border = "1";

            const headerRow = table.insertRow();
            const headers = ["ID", "Engine Type", "Engine Serial Number", "Transmission Type", "Transmission Serial Number", "Body Type", "Color", "Door Count", "VIN"];
            headers.forEach(headerText => {
                const headerCell = document.createElement("th");
                headerCell.appendChild(document.createTextNode(headerText));
                headerRow.appendChild(headerCell);
            });

            cars.forEach(car => {
                const row = table.insertRow();
                row.insertCell(0).appendChild(document.createTextNode(car.id));
                row.insertCell(1).appendChild(document.createTextNode(car.engine.type));
                row.insertCell(2).appendChild(document.createTextNode(car.engine.serialNumber));
                row.insertCell(3).appendChild(document.createTextNode(car.transmission.type));
                row.insertCell(4).appendChild(document.createTextNode(car.transmission.serialNumber));
                row.insertCell(5).appendChild(document.createTextNode(car.body.type));
                row.insertCell(6).appendChild(document.createTextNode(car.body.color));
                row.insertCell(7).appendChild(document.createTextNode(car.body.doorCount));
                row.insertCell(8).appendChild(document.createTextNode(car.body.vin));
            });

            carsListDiv.appendChild(table);
        }
    </script>
</head>
<body>
	<h1>Create New Car</h1>
	<form id="carForm" onsubmit="event.preventDefault(); createCar();">
		<h2>Engine Information</h2>
		<label for="engineType">Type:</label> 
		<input type="text" id="engineType" name="engineType"required><br> <br>
		 <label for="volume">Volume:</label>
		 <input type="text" id="volume" name="volume"required><br> <br> 
		 <label for="powerKw">PowerKw:</label>
		<input type="text" id="powerKw" name="powerKw" required><br> <br>
		<label for="engineSerialNumber">SerialNumber:</label> 
		<input type="text" id="engineSerialNumber" name="engineSerialNumber" required><br>	<br>

		<h2>Transmission Information</h2>
		<label for="transmissionType">Type:</label> <input type="text"	id="transmissionType" name="transmissionType" required><br> <br>
		<label for="transmissionSerialNumber">SerialNumber:</label> 
		<input type="text" id="transmissionSerialNumber" name="transmissionSerialNumber"required><br> <br>

		<h2>Body Information</h2>
		<label for="bodyType">Type:</label> 
		<input type="text" id="bodyType" name="bodyType" required><br> <br> 
		<label for="color">Color:</label>
		<input type="text" id="color" name="color"><br> <br>
		<label for="doorCount">Door Count:</label> <input type="text" id="doorCount" name="doorCount" required><br> <br>
		<label for="vin">VIN:</label> <input type="text" id="vin" name="vin"
			required><br> <br> <input type="submit"	value="Create Car">
	</form>

	<h2>Update Existing Car</h2>
	<label for="id">Enter Id of the Car:</label>
	<input type="text" id="id" name="id" required>
	<br>
	<br>
	<button type="button" onclick="getCarById()">Split car into
		components</button>
	<button type="button" onclick="getAllCars()">Display Created
		Cars</button>


	<h2>List of Created Cars</h2>
	<div id="carsList"></div>
</body>
</html>
