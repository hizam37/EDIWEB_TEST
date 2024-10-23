<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Car Components</title>
<link rel="stylesheet" href="styles/myStyle.css">
 <script>
	let currentCar;

	window.onload = function() {
		const carData = JSON.parse(localStorage.getItem('carData'));
		if (carData) {
			currentCar = carData;
			displayCarData(carData);
			localStorage.removeItem('carData');
		} else {
			alert("No car data found.");
		}
	};

	function displayCarData(car) {
		document.getElementById('carId').innerText = "Car ID: " + car.id;
		document.getElementById('engineType').value = car.engine.type;
		document.getElementById('engineVolume').value = car.engine.volume;
		document.getElementById('enginePowerKw').value = car.engine.powerKw;
		document.getElementById('engineSerialNumber').value = car.engine.serialNumber;

		document.getElementById('transmissionType').value = car.transmission.type;
		document.getElementById('transmissionSerialNumber').value = car.transmission.serialNumber;

		document.getElementById('bodyType').value = car.body.type;
		document.getElementById('bodyColor').value = car.body.color;
		document.getElementById('bodyDoorCount').value = car.body.doorCount;
		document.getElementById('bodyVin').value = car.body.vin;
	}

	async function updateCar() {
		const updatedCar = {
			id : currentCar.id,
			engine : {
				type : document.getElementById('engineType').value,
				volume : parseFloat(document.getElementById('engineVolume').value),
				powerKw : parseFloat(document.getElementById('enginePowerKw').value),
				serialNumber : document.getElementById('engineSerialNumber').value,
			},
			transmission : {
				type : document.getElementById('transmissionType').value,
				serialNumber : document.getElementById('transmissionSerialNumber').value,
			},
			body : {
				type : document.getElementById('bodyType').value,
				color : document.getElementById('bodyColor').value,
				doorCount : parseInt(document.getElementById('bodyDoorCount').value),
				vin : document.getElementById('bodyVin').value,
			}
		};

		const response = await fetch('/car-factory-web/api/cars/update', {
			method : 'PUT',
			headers : {
				'Content-Type' : 'application/json'
			},
			body : JSON.stringify(updatedCar)
		});

		if (response.ok) {
			alert("Car updated successfully!");
		} else {
			alert("Failed to update car.");
		}
	}

	async function deleteTransmission() {
		const response = await fetch(`/car-factory-web/api/cars/deleteTransmissionById/` + currentCar.transmission.id, {
			method: 'DELETE',
		});

		if (response.ok) {
			alert(`Transmission deleted successfully!`);
		} else {
			alert(`Failed to delete Transmission.`);
		}
	}

	async function deleteEngine() {
		const response = await fetch(`/car-factory-web/api/cars/deleteEngineById/` + currentCar.engine.id, {
			method: 'DELETE',
		});

		if (response.ok) {
			alert(`Engine deleted successfully!`);
		} else {
			alert(`Failed to delete Engine.`);
		}
	}

	async function deleteBody() {
		const response = await fetch(`/car-factory-web/api/cars/deleteBodyById/` + currentCar.body.id, {
			method: 'DELETE',
		});

		if (response.ok) {
			alert(`Body deleted successfully!`);
		} else {
			alert(`Failed to delete Body.`);
		}
	}

	function redirectToMainPage() {
		window.location.href = 'http://127.0.0.1:8080/car-factory-web/';
	}

	async function getAllCars(sortBy) {
        const response = await fetch(`/car-factory-web/api/cars/getCars${sortBy ? "/sortBy/" + sortBy : ""}`, {
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
		const headers = ["ID", "Engine Type", "Engine Volume", "Engine Power", "Engine Serial Number", 
	                     "Transmission Type", "Transmission Serial Number", "Body Type", "Color", 
	                     "Door Count", "VIN"];
		headers.forEach(headerText => {
			const headerCell = document.createElement("th");
			headerCell.appendChild(document.createTextNode(headerText));
			headerRow.appendChild(headerCell);
		});

		cars.forEach(car => {
			const row = table.insertRow();

			const cellsData = [
				car.id,
				car.engine.type,
				car.engine.volume,
				car.engine.powerKw,
				car.engine.serialNumber,
				car.transmission.type,
				car.transmission.serialNumber,
				car.body.type,
				car.body.color,
				car.body.doorCount,
				car.body.vin
			];

			cellsData.forEach((data, index) => {
				const cell = row.insertCell(index);
				const span = document.createElement('span');
				span.textContent = data;
				span.style.cursor = 'pointer';
				span.addEventListener('click', () => selectCellValue(car, index));
				cell.appendChild(span);
			});
		});

		carsListDiv.appendChild(table);
	}


	function selectCellValue(carData, index) {
		const fieldIds = [
			'engineType',
			'engineVolume',
			'enginePowerKw',
			'engineSerialNumber',
			'transmissionType',
			'transmissionSerialNumber',
			'bodyType',
			'bodyColor',
			'bodyDoorCount',
			'bodyVin'
		];

		switch(index) {
			case 1: 
				// Similar handling for Engine
				document.getElementById(fieldIds[0]).value = carData.engine.type; // Engine Type
				document.getElementById(fieldIds[1]).value = carData.engine.volume; // Engine Volume
				document.getElementById(fieldIds[2]).value = carData.engine.powerKw; // Engine Power Kw
				document.getElementById(fieldIds[3]).value = carData.engine.serialNumber; // Engine Serial Number
				break;
			case 5: 
				// Similar handling for Transmission
				document.getElementById(fieldIds[4]).value = carData.transmission.type; // Transmission Type
				document.getElementById(fieldIds[5]).value = carData.transmission.serialNumber; // Transmission Serial Number
				break;
			case 7: 
				// Similar handling for Body
				document.getElementById(fieldIds[6]).value = carData.body.type; // Body Type
				document.getElementById(fieldIds[7]).value = carData.body.color; // Body Color
				document.getElementById(fieldIds[8]).value = carData.body.doorCount; // Body Door Count
				document.getElementById(fieldIds[9]).value = carData.body.vin; // VIN
				break;
		}
	}


    function sortCars() {
        const sortBy = document.getElementById('sortSelect').value;
        getAllCars(sortBy);
    }
    </script>
</head>
<body>

	<h1>Car Modification Section</h1>
	
	<h1>Update the car using the sorted list and then click on the component you want to change</h1>
   
	<div class="car-component" id="carId"></div>
 
	<div class="car-component">
		<div class="component-title">Engine Information</div>
		<input type="text" id="engineType" placeholder="Engine Type" /> <input
			type="number" id="engineVolume" placeholder="Engine Volume (L)" /> <input
			type="number" id="enginePowerKw" placeholder="Engine Power (kW)" />
		<input type="text" id="engineSerialNumber"
			placeholder="Engine Serial Number" />
		<button type="button" onclick="deleteEngine()">Remove Engine</button>
	</div>

	<div class="car-component">
		<div class="component-title">Transmission Information</div>
		<input type="text" id="transmissionType"
			placeholder="Transmission Type" /> <input type="text"
			id="transmissionSerialNumber"
			placeholder="Transmission Serial Number" />
		<button type="button" onclick="deleteTransmission()">Remove Transmission</button>
	</div>

	<div class="car-component">
		<div class="component-title">Body Information</div>
		<input type="text" id="bodyType" placeholder="Body Type" /> <input
			type="text" id="bodyColor" placeholder="Body Color" /> <input
			type="number" id="bodyDoorCount" placeholder="Door Count" /> <input
			type="text" id="bodyVin" placeholder="VIN" />
		<button type="button" onclick="deleteBody()">Remove Body</button>
	</div>

	<button type="button" onclick="updateCar()">Update Car</button>
	<button type="button" onclick="redirectToMainPage()">Create New Car</button>
	
	<h2>Sort Cars</h2>
    <label for="sortSelect">Sort By:</label>
    <select id="sortSelect">
        <option value="">Select a component</option>
        <option value="type">type</option>
        <option value="serialNumber">serialNumber</option>
        <option value="vin">vin</option>
    </select>
    <button type="button" onclick="sortCars()">Sort</button>

    <h2>List of Created Cars</h2>
    <div id="carsList"></div>

</body>
</html>
