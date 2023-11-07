var app = angular.module('myApp', []);


app.controller('ManageBaristasController', function($scope, $http, $q) {
	// HTML expects accounts to be formatted as an object with the username field. 
	$scope.accounts = [
		{ username: "Barista1" },
		{ username: "DummyBarista" }
	]
	$scope.formData = { username: "", password: "" } // Use these to store the state of username and password
	$scope.invalid = { username: false, password: false } // Use these to highlight input as required/red

	// Used by the Input elements, when a textbox is clicked to edit, reset invalid.
	$scope.resetInvalid = function(field) {
		$scope.invalid[field] = false;
	}

	// To hide error, set error equal to null. Displays otherwise.
	$scope.error = "";


	$scope.fetchBaristaAccounts = function() {
		$http.get("/api/v1/IDontKnowTheEndpointOfThis").then(function(response) {
			$scope.accounts = response.data;
		});
	}
	
	$scope.onSubmit = function() {
		$scope.error = "An error has occured";
		
		let invalidUsername = $scope.formData.username == "";
		let invalidPassword = $scope.formData.password == "";
		
		if (invalidUsername) $scope.invalid.username = true;
		if (invalidPassword) $scope.invalid.password = true;
		
		if (invalidUsername || invalidPassword) {
			$scope.error = "An account with that username already exists"
		} else {
			$scope.error = "";
			$scope.formData.username = "";
			$scope.formData.password = "";
		};
	}

});