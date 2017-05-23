var jsons = {};

$(document).ready(function(){
    $('input[name=technology]').click(function() {
    	var technology = $(this).attr('value');
    	var checked = $(this).prop('checked');
    	if (checked) {
    		var json;
    		if (!jsons.hasOwnProperty(technology)) {
    			$.ajax({
    				url: 'http://localhost:8000/json/' + technology + '.json',
				    dataType: 'json',
				    async: false,
    				success: function(data) {
		    			jsons[technology] = data;
		    		}
		    	});
    		}
    		json = jsons[technology];

	    	var parentClazz;
	    	$('tr').each(function() {
	    		if ($(this).prop('class') == 'header') {
	    			parentClazz = $(this).find('th:first-child').text();
	    			$(this).append('<td class="' + technology + '"><b>' + technology + '</b></td>');
	    		} else {
	    			var clazz = $(this).prop('class');
	    			if (json[parentClazz] && json[parentClazz].hasOwnProperty(clazz)) {
	    				//alert(JSON.stringify(json[parentClazz][clazz]));

	    				$(this).append('<td class="' + technology + '">' + json[parentClazz][clazz] + '</td>');
	    			} else {
	    				$(this).append('<td class="' + technology + '"></td>');
	    			}
	    		}
	    	});
	    } else {
	    	$("." + technology).remove();
	    }
    });
});