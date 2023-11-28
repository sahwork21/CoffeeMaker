var app = angular.module('myApp', []);

app.controller('OrderHistoryController', function($scope, $http, $q) {
	// HTML expects accounts to be formatted as an object with the username field. 
	$scope.orders = [{'id':69420, 'status': 'Completed', date: '10:25 PM November 20, 2023', recipe:'Coffee', customer:'Karen'}, {'id':69420, 'status': 'Completed', date: '10:25 PM November 20, 2023', recipe:'Coffee', customer:'Karen'}];
	$scope.popularRecipes = [ ["Coffee", 5], ["Latte", 2 ], ["Water", 2] ] // Populate this with the top 3 (max) recipes



	$scope.fetchOrders = function() {
		$http.get("/api/v1/users/barista").then(function(response) {
			$scope.accounts = response.data;
			console.log(response.data);
		}).catch(function(err) {
			console.log(err);
		});
		
		//Go get the orders information and change up the page
	}
	
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
  	var chart1 = document.getElementById('chart1').getContext('2d');

  	// Create a new chart instance using AngularJS $scope variables
  	$scope.myChart = new Chart(chart1, {
    	type: 'bar',
    	data: $scope.chartData,
    	options: $scope.chartOptions
  	});
  	
  	
  	// Get the canvas element
  	var chart2 = document.getElementById('chart2').getContext('2d');
  
	$scope.chart2Data = {
    	labels: ["Coffee", "Latte", "Water"], // The top 3 recipes most popular
    	datasets: [{
		      label: 'Orders',
		      data: [5, 3, 2], // The recipes # orders 
		      backgroundColor: ['rgba(130, 100, 65, 1)', 'rgba(65, 100, 130, 1)', 'rgba(255, 190, 80, 1)'],
		      borderColor: 'rgba(255, 255, 255, 1)',
		      borderWidth: 3,
    	}]
  	};
  	

  // Create a new pie chart instance using AngularJS $scope variables
  $scope.chart2 = new Chart(chart2, {
    type: 'pie', // Set chart type to 'pie'
    data: $scope.chart2Data,
    options: $scope.chartOptions
  });

});