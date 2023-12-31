var app = angular.module('myApp', []);


app.controller('ManagerController', function($scope, $http, $q) {
	$scope.actions = [
		{ title: "Add inventory", description: "Running low?", "icon": ("M0 32C0 14.3 14.3 0 32 0h72.9c27.5 0 52 17.6 60.7 43.8L257.7 320c30.1 .5 56.8 14.9 74 37l202.1-67.4c16.8-5.6 34.9 3.5 40.5 20.2s-3.5 34.9-20.2 40.5L352 417.7c-.9 52.2-43.5 94.3-96 94.3c-53 0-96-43-96-96c0-30.8 14.5-58.2 37-75.8L104.9 64H32C14.3 64 0 49.7 0 32zM244.8 134.5c-5.5-16.8 3.7-34.9 20.5-40.3L311 79.4l19.8 60.9 60.9-19.8L371.8 59.6l45.7-14.8c16.8-5.5 34.9 3.7 40.3 20.5l49.4 152.2c5.5 16.8-3.7 34.9-20.5 40.3L334.5 307.2c-16.8 5.5-34.9-3.7-40.3-20.5L244.8 134.5z"), "viewBox": "0 0 576 512", href: "/addInventory", color: "-blue-500" },
		{ title: "Add Recipe", description: "Have a creative idea?", "icon": "M0 80v48c0 17.7 14.3 32 32 32H48 96V80c0-26.5-21.5-48-48-48S0 53.5 0 80zM112 32c10 13.4 16 30 16 48V384c0 35.3 28.7 64 64 64s64-28.7 64-64v-5.3c0-32.4 26.3-58.7 58.7-58.7H480V128c0-53-43-96-96-96H112zM464 480c61.9 0 112-50.1 112-112c0-8.8-7.2-16-16-16H314.7c-14.7 0-26.7 11.9-26.7 26.7V384c0 53-43 96-96 96H368h96z", "viewBox": "0 0 576 512", href: "/addRecipe", color: "-amber-500" },
		{ title: "Edit Recipe", description: "Make recipe changes", "icon": "M471.6 21.7c-21.9-21.9-57.3-21.9-79.2 0L362.3 51.7l97.9 97.9 30.1-30.1c21.9-21.9 21.9-57.3 0-79.2L471.6 21.7zm-299.2 220c-6.1 6.1-10.8 13.6-13.5 21.9l-29.6 88.8c-2.9 8.6-.6 18.1 5.8 24.6s15.9 8.7 24.6 5.8l88.8-29.6c8.2-2.7 15.7-7.4 21.9-13.5L437.7 172.3 339.7 74.3 172.4 241.7zM96 64C43 64 0 107 0 160V416c0 53 43 96 96 96H352c53 0 96-43 96-96V320c0-17.7-14.3-32-32-32s-32 14.3-32 32v96c0 17.7-14.3 32-32 32H96c-17.7 0-32-14.3-32-32V160c0-17.7 14.3-32 32-32h96c17.7 0 32-14.3 32-32s-14.3-32-32-32H96z", href: "/editrecipe", color: "-indigo-500" },
		{ title: "Delete Recipe", description: "Delete an existing recipe", "icon": "M135.2 17.7L128 32H32C14.3 32 0 46.3 0 64S14.3 96 32 96H416c17.7 0 32-14.3 32-32s-14.3-32-32-32H320l-7.2-14.3C307.4 6.8 296.3 0 284.2 0H163.8c-12.1 0-23.2 6.8-28.6 17.7zM416 128H32L53.2 467c1.6 25.3 22.6 45 47.9 45H346.9c25.3 0 46.3-19.7 47.9-45L416 128z", "viewBox": "0 0 448 512", href: "/deleteRecipe", color: "-green-700" },
		{ title: "Add Ingredient", description: "Pumpkin spice season", "icon": "M448 96c0-35.3-28.7-64-64-64c-6.6 0-13 1-19 2.9c-22.5 7-48.1 14.9-71 9c-75.2-19.1-156.4 11-213.7 68.3S-7.2 250.8 11.9 326c5.8 22.9-2 48.4-9 71C1 403 0 409.4 0 416c0 35.3 28.7 64 64 64c6.6 0 13-1 19.1-2.9c22.5-7 48.1-14.9 71-9c75.2 19.1 156.4-11 213.7-68.3s87.5-138.5 68.3-213.7c-5.8-22.9 2-48.4 9-71c1.9-6 2.9-12.4 2.9-19.1zM212.5 127.4c-54.6 16-101.1 62.5-117.1 117.1C92.9 253 84 257.8 75.5 255.4S62.2 244 64.6 235.5c19.1-65.1 73.7-119.8 138.9-138.9c8.5-2.5 17.4 2.4 19.9 10.9s-2.4 17.4-10.9 19.9z", "viewBox": "0 0 448 512", href: "/addIngredient", color: "-yellow-500" },
		{ title: "Manage Baristas", description: "Add new member", "icon": "M144 0a80 80 0 1 1 0 160A80 80 0 1 1 144 0zM512 0a80 80 0 1 1 0 160A80 80 0 1 1 512 0zM0 298.7C0 239.8 47.8 192 106.7 192h42.7c15.9 0 31 3.5 44.6 9.7c-1.3 7.2-1.9 14.7-1.9 22.3c0 38.2 16.8 72.5 43.3 96c-.2 0-.4 0-.7 0H21.3C9.6 320 0 310.4 0 298.7zM405.3 320c-.2 0-.4 0-.7 0c26.6-23.5 43.3-57.8 43.3-96c0-7.6-.7-15-1.9-22.3c13.6-6.3 28.7-9.7 44.6-9.7h42.7C592.2 192 640 239.8 640 298.7c0 11.8-9.6 21.3-21.3 21.3H405.3zM224 224a96 96 0 1 1 192 0 96 96 0 1 1 -192 0zM128 485.3C128 411.7 187.7 352 261.3 352H378.7C452.3 352 512 411.7 512 485.3c0 14.7-11.9 26.7-26.7 26.7H154.7c-14.7 0-26.7-11.9-26.7-26.7z", "viewBox": "0 0 640 512", href: "/manageBaristas", color: "-stone-500" },
	]
	
	$scope.ordersData = [{'id': 123}];
	
	$scope.popularRecipes = [ ["Coffee", 5], ["Latte", 2 ], ["Water", 2] ] // Populate this with the top 3 (max) recipes

	$scope.fetchOrders = function() {
		$http.get("/api/v1/orders").then(function(response) {
			console.log(response);
			$scope.orders = response.data;
			console.log($scope.orders);
			
			//Modify the most popular recipes
			$scope.findPopular();
			
			
			//Modify the total orders number
			
		});
		
		// Populate the most popular recipes from from our list of historic orders
	}

	$scope.fetchOrders();
	
	
	// Copy of most popular finding
	// Function to find popular recipes based on order data
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
    	//$scope.updatePieChartData();
	};
});