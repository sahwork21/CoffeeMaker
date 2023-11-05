var app = angular.module('myApp', []);


app.controller('SignInController', function($scope, $http, $q) {
	$scope.error = null// Populating this variable displays the error with the message
	$scope.formData = {username: "", password: ""} // Use these to store the state of username and password
	$scope.invalid = {username: false, password: false} // Use these to highlight input as required/red
	
	$scope.functionName = function() {
		$http.get("/api/v1/").then(function(response) { // Template API GET
			
		});
	}
	
	// Used by the Input elements, when a textbox is clicked to edit, reset invalid.
	$scope.resetInvalid = function(field) {
		$scope.invalid[field] = false;
	}
	
	$scope.onSubmit = function() {
		
		$scope.error = "Username or password is incorrect";
		
		let invalidUsername = $scope.formData.username == "";
		let invalidPassword = $scope.formData.password == "";
		
		if (invalidUsername) $scope.invalid.username = true;
		if (invalidPassword) $scope.invalid.password = true;
		
		if (invalidUsername || invalidPassword) {
			$scope.error = "Username or password is incorrect"
		} else {
			$scope.error = "";
		};
		
	}
	
});