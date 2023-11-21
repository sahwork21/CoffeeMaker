var app = angular.module('myApp', []);

app.controller('OrderHistoryController', function($scope, $http, $q) {
	// HTML expects accounts to be formatted as an object with the username field. 
	$scope.orders = [{'id':69420, 'status': 'Completed', date: '10:25 PM November 20, 2023', recipe:'Coffee', customer:'Karen'}, {'id':69420, 'status': 'Completed', date: '10:25 PM November 20, 2023', recipe:'Coffee', customer:'Karen'}];
	$scope.popularRecipes = [ ["Coffee", 5], ["Latte", 2 ] ] // Populate this with the top 3 (max) recipes



	$scope.fetchOrders = function() {
		$http.get("/api/v1/users/barista").then(function(response) {
			$scope.accounts = response.data;
			console.log(response.data);
		}).catch(function(err) {
			console.log(err);
		});
	}


});