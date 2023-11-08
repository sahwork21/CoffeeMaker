var app = angular.module('myApp', []);


app.controller('APIUserController', function($scope, $http, $q) {
	$scope.error = null; // Populating this variable displays the error with the message
	$scope.formData = {username: "", password: ""}; // Use these to store the state of username and password
	$scope.invalid = {username: false, password: false}; // Use these to highlight input as required/red

	
	// Used by the Input elements, when a textbox is clicked to edit, reset invalid.
	$scope.resetInvalid = function(field) {
		$scope.invalid[field] = false;
	}
	
	$scope.onSubmit = function() {
		$http.get("/api/v1/users/" + $scope.formData.username, $scope.formData.password).then(function(response) {
			$scope.user = response.data;
		}, function(rejection) {
			$scope.error = rejection.data.message
		});
		
	}
	
});