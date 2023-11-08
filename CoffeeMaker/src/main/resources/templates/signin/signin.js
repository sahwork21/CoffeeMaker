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
		
		
		
		
		$http.get("/api/v1/users/" + $scope.formData.username + "/" + $scope.formData.password).then(function(response) {
			console.log("Successful login loop")
			let user = response.data;
			
			
			//We have to save their login credentials so we can get things like orders and stuff if they are a customer
			//These credentials should only persist for the tab with sessionStorage
			//Only save it for the customer in the future since we have to reget their orders history
			let username = $scope.formData.username;
			let password = $scope.formData.password;
			
			sessionStorage.setItem("username", username);
			sessionStorage.setItem("password", password);
			console.log(sessionStorage.getItem("username"));
			console.log(sessionStorage.getItem("password"));
			
			console.log(user);
			
			
			//We need to force a redirect of the page after logging them in
			//Just use /*location.href = "barista.html"*/ to redirect
			if(user.roleType == "CUSTOMER"){
				console.log("In Customer redirect");
				location.href = "customer.html";
			} 
			else if (user.roleType == "MANAGER") {
				console.log("In Manager redirect");
				location.href = "manager.html";
			}
			else if (user.roleType == "BARISTA"){
				console.log("In Barista redirect");
				location.href = "barista.html";
			};
			
			
			
			
			$scope.error = null;
			$scope.user = response.data;
			$scope.success = "Logged in successfully, but you should be redireceted. Look at the javascript because something went wrong.";
		}, function(rejection) {
			console.log("Rejection of login loop");
			$scope.success = null;
			$scope.error = rejection.data.message
		});
		
	}
	
});