var globaldata;
var jsons = {};

$(document).ready(function(){
    $('input[name=technology]').click(function() {
    	var technology = $(this).attr('value');
    	var checked = $(this).prop('checked');
    	if (checked) {
    		var json;
    		if (jsons.hasOwnProperty(technology)) {
    			json = jsons[technology];
    		} else {
	    		$.getJSON('http://localhost:8000/json/' + technology + '.json', function(data) {
		    		globaldata = data;
		    		jsons[technology] = data;
		    		/*for (var key in data) {
		    			alert(key);
		    			alert(JSON.stringify(data[key]));
		    		}*/
		    	});
    		}
	    	$('tr').each(function() {
	    		$(this).append('<td class="' + technology + '">new</td>');
	    	});
	    } else {
	    	$("." + technology).remove();
	    }
    });
});