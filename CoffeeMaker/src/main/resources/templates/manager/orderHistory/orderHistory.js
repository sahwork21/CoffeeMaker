var app = angular.module('myApp', []);

app.controller('OrderHistoryController', function($scope, $http, $q) {
	// HTML expects accounts to be formatted as an object with the username field. 
	$scope.orders = [{'id':69420, 'status': 'Completed', date: '10:25 PM November 20, 2023', recipe:'Coffee', customer:'Karen'}, {'id':69420, 'status': 'Completed', date: '10:25 PM November 20, 2023', recipe:'Coffee', customer:'Karen'}];
	$scope.popularRecipes = [ ["Coffee", 2], ["Latte", 1 ], ["Water", 1] ] // Populate this with the top 3 (max) recipes


	// Function to fetch orders from the server
	$scope.fetchOrders = function() {
		//Fetch all the orders possible
		$http.get("/api/v1/orders").then(function(response) {
			$scope.orders = response.data;
			
			//Clean the status labels for a pretty picture
			$scope.orders.forEach(order=>{
				if(order.status === "UNFULFILLED"){
					order.status = "Incomplete";
				}else if(order.status === "READY_TO_PICKUP"){
					order.status = "Ready To Pickup";
				}else{
					order.status = "Completed";
				}
			});
			
			// Call findPopular after updating orders
			$scope.findPopular(); 
			$scope.barGraph();
		}, function(rejection) {
			console.error(rejection.data.message);
		});
	}
	
	// Function to update the bar graph
	$scope.barGraph = function(){
		// Update the chart data labels 
		// Count up the instances of time for each order we have
            $scope.orders.forEach(order=>{
				// Get the orders time
				// Get the hour and am or pm
				
				//Verify that the time is a value of 1 to 12
				//Find hours by splitting at the colon :
				
				console.log(order.placedAt);
				
				var hour = order.placedAt.split(':')[0];
				
				// Parse for the int in the hour value
				var hourValue = parseInt(hour, 10);
				
				console.log(hourValue);
				
				
				
				// If the value is 8 - 11 am do this
				/*if(order.placedAt.contains("am") && (hourValue >= 8 && hourValue <= 11)){
					// am loop
					
				}*/
				
				// If it is 12 - 8pm add 12 to the time so we can decide later if valid
				// Should work for 24 hour time too
				if(order.placedAt.includes("PM") && hourValue < 12){
					hourValue += 12;
				}
				
				// We only accept hourValue from 8 to 20
				if(hourValue >= 8 && hourValue <= 20){
					// Now modify the bar graph
					// Access the chartData's dataset 0 and increase the data which has order values
					$scope.chartData.datasets[0].data[hourValue - 8]++;
				}
			});
		
		
		// Update the bar graph with the chart data based on times
		$scope.chart1 = new Chart(chart1, {
		    type: 'bar', // Set chart type to 'pie'
		    data: $scope.chartData,
		    options: $scope.chartOptions
		});
	}
	
	
	// Function to find popular recipes based on order data
	$scope.findPopular = function() {
	    var recipeCounts = {}; // Object to store the count of each recipe
	
	    // Iterate through orders and count occurrences of each recipe
	    $scope.orders.forEach(function(order) {
	        var recipe = order.recipe;
	        if (recipeCounts[recipe]) {
	            recipeCounts[recipe]++;
	        } else {
	            recipeCounts[recipe] = 1;
	        }
	    });
	
	    // Convert the recipeCounts object to an array of [recipe, count] pairs
	    var recipeArray = [];
	    for (var recipe in recipeCounts) {
	        recipeArray.push([recipe, recipeCounts[recipe]]);
	    }
	
	    // Sort the recipeArray by count in descending order
	    recipeArray.sort(function(a, b) {
	        return b[1] - a[1];
	    });
	
	    // Take the top 3 recipes (or fewer if there are less than 3)
	    var top3Recipes = recipeArray.slice(0, 3);
	
	    // Update $scope.popularRecipes
	    $scope.popularRecipes = top3Recipes;
	    // Update Pie chart data based on popularRecipes
    	$scope.updatePieChartData();
	};
	
	// Dummy data for a bar chart
	$scope.chartData = {
    	labels: ["8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM"],
    	datasets: [{
		      label: 'Orders',
		      data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
		      backgroundColor: 'rgba(75, 192, 192, 0.2)',
		      borderColor: 'rgba(75, 192, 192, 1)',
		      borderWidth: 1,
    	}]
  	};

  	// Configuration options
  	$scope.chartOptions = {
	  scales: {
	    x: {
	      display: false
	    },
	    y: {
	      display: false
	    },
	  },
	  responsive: true,
	  plugins: {
	    legend: {
	      display: false,
	    },
	    title: {
	      display: false,
	      text: 'Chart.js Bar Chart',
	    },
	  },
  	};

  	// Get the canvas elements
	var chart2 = document.getElementById('chart2').getContext('2d');
	
	var chart1 = document.getElementById('chart1').getContext('2d');
	
	// Initialize data for the pie chart
	$scope.chart2Data = {
	    labels: [], // Initialize labels array
	    datasets: [{
	        label: 'Orders',
	        data: [], // Initialize data array
	        backgroundColor: [], // Initialize backgroundColor array
	        borderColor: 'rgba(255, 255, 255, 1)',
	        borderWidth: 3,
	    }]
	};
	
	// Update Pie chart data based on popularRecipes
	$scope.updatePieChartData = function() {
	    // Extract recipe names for labels
	    $scope.chart2Data.labels = $scope.popularRecipes.map(function(recipe) {
	        return recipe[0];
	    });
	
		// Extract recipe counts for data	
	    $scope.chart2Data.datasets[0].data = $scope.popularRecipes.map(function(recipe) {
	        return recipe[1];
	    });
	
	    // You can set custom colors for each recipe, or use a color palette
	    $scope.chart2Data.datasets[0].backgroundColor = [
	        'rgba(130, 100, 65, 1)',
	        'rgba(65, 100, 130, 1)',
	        'rgba(255, 190, 80, 1)'
	    ];
	    
	    // Create a new pie chart instance using AngularJS $scope variables
		$scope.chart2 = new Chart(chart2, {
		    type: 'pie', // Set chart type to 'pie'
		    data: $scope.chart2Data,
		    options: $scope.chartOptions
		});
	};
	
	// Call fetchOrders to initialize the data
    $scope.fetchOrders();
});