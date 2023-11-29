var app = angular.module('myApp', []);

app.controller('OrderHistoryController', function($scope, $http, $q) {
	// HTML expects accounts to be formatted as an object with the username field. 
	$scope.orders = [{'id':69420, 'status': 'Completed', date: '10:25 PM November 20, 2023', recipe:'Coffee', customer:'Karen'}, {'id':69420, 'status': 'Completed', date: '10:25 PM November 20, 2023', recipe:'Coffee', customer:'Karen'}];
	$scope.popularRecipes = [ ["Coffee", 2], ["Latte", 1 ], ["Water", 1] ] // Populate this with the top 3 (max) recipes



	$scope.fetchOrders = function() {
		$http.get("/api/v1/orders/history").then(function(response) {
			$scope.orders = response.data;
			$scope.findPopular(); // Call findPopular after updating orders
		}, function(rejection) {
			console.error(rejection.data.message);
		});
	}
	
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
	
	$scope.chartData = {
    	labels: ["8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM"],
    	datasets: [{
		      label: 'Orders',
		      data: [2, 4, 5, 8, 15, 17, 15, 16, 14, 12, 10, 10, 6],
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

  	// Get the canvas element
	var chart2 = document.getElementById('chart2').getContext('2d');
	
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
	    $scope.chart2Data.labels = $scope.popularRecipes.map(function(recipe) {
	        return recipe[0]; // Extract recipe names
	    });
	
	    $scope.chart2Data.datasets[0].data = $scope.popularRecipes.map(function(recipe) {
	        return recipe[1]; // Extract recipe counts
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
	
    $scope.fetchOrders();
});