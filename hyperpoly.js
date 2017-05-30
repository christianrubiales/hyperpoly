var jsons = {};
var selected = [];

$(document).ready(function(){

	// make sure checkboxes are unchecked
	$('input[name=technology]').prop('checked', false);

	// read params
	var params={};
	location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(s,k,v) { params[k] = v });
	if (params['q']) {
		params['q'] = params['q'].replace(/\//, '');
	} else {
		params['q'] = 'java,python'
		location.search = '?q=java,python';
		selected.push('java');
		selected.push('python');
	}

	$.updateShortUrl = function () {
		var href = '';

		for (var i = 0; i < selected.length - 1; i++) {
			href += selected[i] + ',';
		}
		var loc = window.location;
		if (selected.length > 0) {
			href += selected[selected.length - 1];
			$('#shortcuturl').attr('href', './q='+ href);
			$('#shortcuturl').text(loc.protocol + '//' + loc.host + loc.pathname +'?q='+ href);
		} else {
			$('#shortcuturl').attr('href', '.');
			$('#shortcuturl').text(loc.protocol + '//' + loc.host + loc.pathname);
		}


	}

    $('input[name=technology]').click(function() {
    	var technology = $(this).attr('value');
    	var checked = $(this).prop('checked');
    	if (checked) {
    		var json;
    		if (!jsons.hasOwnProperty(technology)) {
    			$.ajax({
    				url: './json/' + technology + '.json',
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
	    			parentClazz = $(this).find('th:first-child').attr('name');
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
	    	if (selected.indexOf(technology) < 0) {
	    		selected.push(technology);
	    	}
	    } else {
	    	$("." + technology).remove();
	    	selected.splice(selected.indexOf(technology), 1);
	    }
	    $.updateShortUrl();
    });

	// check techs from params
	if (params['q']) {
		var langs = params['q'].split(',');
		langs.forEach(function (lang) {
			$('input[value=' + lang + ']').trigger('click');
		});
	}
});