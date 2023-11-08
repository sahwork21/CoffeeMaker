var app = angular.module('myApp', []);


app.controller('APIUserController', function($scope, $http, $q) {
	$scope.success = null; // Populating this variable displays the success with the message
	$scope.error = null; // Populating this variable displays the error with the message
	$scope.formData = {username: "", password: "", roleType: 0}; // Use these to store the state of username and password
	$scope.invalid = {username: false, password: false}; // Use these to highlight input as required/red
	
	
	// Used by the Input elements, when a textbox is clicked to edit, reset invalid.
	$scope.resetInvalid = function(field) {
		$scope.invalid[field] = false;
	}
	
	$scope.onSubmit = function() {
		$http.post("/api/v1/users/", $scope.formData).then(function(success) {
			$scope.error = null;
			console.log(success.data.message);
			$scope.success = success.data.message; // Set the success message
		}, function(rejection){
			$scope.success = null;
			$scope.error = rejection.data.message
		});
	}
	
});