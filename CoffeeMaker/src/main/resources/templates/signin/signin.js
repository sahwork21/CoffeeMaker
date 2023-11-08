var app = angular.module('myApp', []);


app.controller('SignInController', function($scope, $http, $q) {
	$scope.success = null; // Populating this variable displays the success with the message
	$scope.error = null; // Populating this variable displays the error with the message
	$scope.formData = {username: "", password: ""}; // Use these to store the state of username and password
	$scope.invalid = {username: false, password: false}; // Use these to highlight input as required/red

	
	// Used by the Input elements, when a textbox is clicked to edit, reset invalid.
	$scope.resetInvalid = function(field) {
		$scope.invalid[field] = false;
	}
	
	$scope.onSubmit = function() {
		//We need to pass in variables as JSON format for API calls
		let rawPassword = {password:$scope.formData.password};
		
		console.log(rawPassword);
		
		
		$http.get("/api/v1/users/" + $scope.formData.username + "/" + $scope.formData.password).then(function(response) {
			console.log("Successful login loop")
			let user = response.data;
			
			
			//We have to save their login credentials so we can get things like orders and stuff if they are a customer
			//These credentials should only persist for the tab with sessionStorage
			let username = $scope.formData.username;
			
			console.log(user);
			
			sessionStorage.setItem("username", username);
			sessionStorage.setItem("password", rawPassword);
				
			//We need to force a redirect of the page after logging them in
			//Just use /*location.href = "barista.html"*/ to redirect
			
			
			
			
			$scope.error = null;
			$scope.user = response.data;
			$scope.success = "Logged in successfully.";
		}, function(rejection) {
			console.log("Rejection of login loop");
			$scope.success = null;
			$scope.error = rejection.data.message
		});
		
	}
	
});