'use strict';

var ws = angular.module('myApp.services',[]);

ws.factory('Tasks', ['$resource', function($resource){
	return {
		listByWPG: function(wpg, callback) {
			var api = $resource("application/scheduler/tasks/wpg/:wpg",{ wpg : wpg });

			api.query({}, function(response){
				callback(response);
			});
		}
	};
}]);


var app = angular.module("myApp", ['ngCookies',
                                   'ngResource',
                                   'ngSanitize',
                                   'ngRoute',
                                   'ui.bootstrap',
                                   'ui.grid',
                                   'ui.grid.selection',
                                   'ui.grid.resizeColumns',
                                   'myApp.services']);

app.config(function($routeProvider){
    $routeProvider
	    .when("/", {
	        controller: "ctrlGeneric",
	        templateUrl: "pages/home.html"
	    })
	    .when("/scheduler", {
	        controller: "ctrlScheduler",
	        templateUrl: "pages/scheduler.html"
	    })
	    .otherwise({
			redirectTo: '/'
		});
})

app.controller("ctrlScheduler", function($scope)
{
});

app.controller("ctrlEngineers", function($scope)
{
	$scope.wpg = '7471264-2-M102';
	
	$scope.tblData = [{w6key:39890558, actname:'Upload and processing FMT File in TIPP tool', duration:2376000}];
	$scope.tblSelections = [];
	$scope.tblColumns = [{ name: 'id', displayName: 'W6KEY', maxWidth: 80 },
	                     { name: 'actname', displayName: 'Name', minWidth: 300 },
	                     { name: 'duration', displayName: 'Duration', maxWidth: 85, cellFilter: 'date : \'HH:mm:ss\' : \'GMT\'' },
	                     //{ name: 'priority', displayName: 'Priority' },
	                     { name: 'earlyStart', displayName: 'Early Start', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\''},
	                     { name: 'lateStart', displayName: 'Late Start', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\'' },
	                     { name: 'dueDate', displayName: 'Due Date', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\'' },
	                     { name: 'workpackagegroup', displayName: 'WPG' }];

	$scope.gridOptionsEng = { 
			data: $scope.tblData, 
			columnDefs: $scope.tblColumns,
			selectedItems: $scope.tblSelections,
			showGridFooter: true,
			multiSelect: false };

});

app.controller("ctrlTasks", function($scope, Tasks)
{
	$scope.wpg = '7471264-2-M102';
	
	$scope.tblData = [{w6key:39890558, actname:'Upload and processing FMT File in TIPP tool', duration:2376000}];
	$scope.tblSelections = [];
	$scope.tblColumns = [{ name: 'id', displayName: 'W6KEY', maxWidth: 80 },
	                     { name: 'actname', displayName: 'Name', minWidth: 300 },
	                     { name: 'duration', displayName: 'Duration', maxWidth: 85, cellFilter: 'date : \'HH:mm:ss\' : \'GMT\'' },
	                     //{ name: 'priority', displayName: 'Priority' },
	                     { name: 'earlyStart', displayName: 'Early Start', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\''},
	                     { name: 'lateStart', displayName: 'Late Start', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\'' },
	                     { name: 'dueDate', displayName: 'Due Date', maxWidth: 90, cellFilter: 'date : \'yyyy-MM-dd\' : \'GMT\'' },
	                     { name: 'workpackagegroup', displayName: 'WPG' }];

	$scope.gridOptionsTasks = { 
			data: $scope.tblData, 
			columnDefs: $scope.tblColumns,
			selectedItems: $scope.tblSelections,
			showGridFooter: true,
			multiSelect: false };

	Tasks.listByWPG($scope.wpg, function(data) {
		$scope.gridOptionsTasks.data = data;
	});
	
	$scope.loadWPG = function() {
		Tasks.listByWPG($scope.wpg, function(data) {
			$scope.gridOptionsTasks.data = data;
		});
    };
});

app.controller("ctrlGeneric", function($scope)
{
	$scope.listEngineers = [ 
			{NAME : 'LUIS',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS2',LASTNAME : 'GONZALEZ'}, 
			{NAME : 'LUIS3',LASTNAME : 'GONZALEZ'}, 
			{NAME : 'LUIS4',LASTNAME : 'GONZALEZ'}, 
			{NAME : 'LUIS5',LASTNAME : 'GONZALEZ'}, 
			{NAME : 'LUIS6',LASTNAME : 'GONZALEZ'}, 
			{NAME : 'LUIS7',LASTNAME : 'GONZALEZ'}, 
			{NAME : 'LUIS8',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS9',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS10',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS11',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS12',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS13',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS14',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS15',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS16',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS17',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS18',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS19',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS20',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS21',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS22',LASTNAME : 'GONZALEZ'},
			{NAME : 'LUIS23',LASTNAME : 'GONZALEZ'}];
    $scope.gridEngineers = { data: 'listEngineers' };
			
	$scope.listActivitiesAssigned = [ 
			{enginner : 'LUIS',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS2',activitie : 'ACIVITIE_2', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS3',activitie : 'ACIVITIE_3', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS4',activitie : 'ACIVITIE_4', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS5',activitie : 'ACIVITIE_5', op1 : "active", op2 : "active", op3 : "", op4 : "", op5 : "", op6 : "", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS5',activitie : 'ACIVITIE_5', op1 : "", op2 : "", op3 : "active", op4 : "active", op5 : "", op6 : "", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS5',activitie : 'ACIVITIE_5', op1 : "", op2 : "", op3 : "", op4 : "", op5 : "active", op6 : "active", op7 : "active", op8 : "active", op9 : "active", op10 : "active"},
			{enginner : 'LUIS6',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS7',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS7',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS8',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS9',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS10',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS11',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS12',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS12',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS12',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS13',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS14',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""},
			{enginner : 'LUIS15',activitie : 'ACIVITIE_1', op1 : "active", op2 : "active", op3 : "active", op4 : "active", op5 : "active", op6 : "active", op7 : "", op8 : "", op9 : "", op10 : ""}];
	$scope.gridActivitiesAssigned = 
    	{
			data: 'listActivitiesAssigned',
			enablePaging: true,
			showFooter: true,
			columnDefs: [
			             { field: 'enginner', displayName: 'ENGINEER NAME'},
			             { field: 'activitie',   displayName: 'ACTIVITIE NAME'},
			             { field: 'op1',  displayName: '09:00'},
			             { field: 'op2', displayName: '10:00'},
			             { field: 'op3', displayName: '11:00'},
			             { field: 'op4', displayName: '12:00'},
			             { field: 'op5', displayName: '13:00'},
			             { field: 'op6', displayName: '14:00'},
			             { field: 'op7', displayName: '15:00'},
			             { field: 'op8', displayName: '16:00'},
			             { field: 'op9', displayName: '17:00'},
			             { field: 'op10', displayName: '18:00'},
			           ],
           filterOptions: {filterText: '', useExternalFilter: false},
           showFilter: true
			//enablePinning: true,
	        //columnDefs: [{ field: "ENGINEER_NAME", pinned: true,field: "activitie" }]
    	};
    	
		$scope.myData = [{name: "Moroni", age: 50},
	                     {name: "Tiancum", age: 43},
	                     {name: "Jacob", age: 27},
	                     {name: "Nephi", age: 29},
	                     {name: "Enos", age: 34}];
	    $scope.gridOptions = { data: 'myData' };				
});

app.controller('myCtrlScroll', ['$scope', '$location', '$anchorScroll',
	function ($scope, $location, $anchorScroll) 
	{
		$scope.gotoBottom = function(){
     	// set the location.hash to the id of
		// the element you wish to scroll to.
	    $location.hash('bottom');
	    // call $anchorScroll()
		$anchorScroll();
		};	
  }]);
