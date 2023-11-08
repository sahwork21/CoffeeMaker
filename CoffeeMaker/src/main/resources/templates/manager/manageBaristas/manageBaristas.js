var app = angular.module('myApp', []);

app.controller('ManageBaristasController', function($scope, $http, $q) {
	// HTML expects accounts to be formatted as an object with the username field. 
	$scope.accounts = [];
	$scope.formData = {username: "", password: "", roleType: 2}; // Use these to store the state of username, password, and role.
	$scope.invalid = { username: false, password: false } // Use these to highlight input as required/red

	// Used by the Input elements, when a textbox is clicked to edit, reset invalid.
	$scope.resetInvalid = function(field) {
		$scope.invalid[field] = false;
	}

	// To hide error, set error equal to null. Displays otherwise.
	$scope.error = "";


	$scope.fetchBaristaAccounts = function() {
		$http.get("/api/v1/users/barista").then(function(response) {
			$scope.accounts = response.data;
			console.log(response.data);
		}).catch(function(err) {
			console.log(err);
		});
	}


	$scope.onSubmit = function() {
		let invalidUsername = $scope.formData.username == "";
		let invalidPassword = $scope.formData.password == "";

		if (invalidUsername) $scope.invalid.username = true;
		if (invalidPassword) $scope.invalid.password = true;
		if (invalidUsername || invalidPassword) return;
				
		
		$http.post("/api/v1/users/", $scope.formData).then(function(success) {
			$scope.error = "";
			$scope.formData.username = "";
			$scope.formData.password = "";
			$scope.invalid.password = false;
			
			
			$scope.fetchBaristaAccounts();
		}).catch(function(err) {
			$scope.invalid.username = true;
			$scope.invalid.password = true;
			$scope.error = "Invalid username or Password";
		});
	}

	$scope.fetchBaristaAccounts();

});