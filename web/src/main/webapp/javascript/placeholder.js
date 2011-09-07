/* This code is licensed under Creative Commons Attribution 3.0    *
 * You may share and remix the script so long as you attribute the *
 * original author, Andrew January.                                *
 * http://www.morethannothing.co.uk/2010/01/placeholder-text-in-html5-a-js-fallback/
 * http://creativecommons.org/licenses/by/3.0/                     */

$(document).ready(function() {
    // Check to see if the browser already supports placeholder text (introduced in HTML5). If it does,
    // then we don't need to do anything.
    var i = document.createElement('input');
    if ('placeholder' in i) {
        return;
    }
    
	var isPassword = function(input) {
		return $(input).attr('realType') == 'password';
	}
	
	var valueIsPlaceholder = function(input) {
		return input.value == $(input).attr('placeholder');
	}

	var showPlaceholder = function(input, loading) {
		// FF and IE save values when you refresh the page. If the user refreshes the page
		// with the placeholders showing they will be the default values and the input fields won't
		// be empty. Using loading && valueIsPlaceholder is a hack to get around this and highlight
		// the placeholders properly on refresh.
		if (input.value == '' || (loading && valueIsPlaceholder(input))) {
			if (isPassword(input)) {
				// Must use setAttribute rather than jQuery as jQuery throws an exception
				// when changing type to maintain compatability with IE.
				// We use our own "compatability" method by simply swallowing the error.
				try {
					input.setAttribute('type', 'input');
				} catch (e) { }
			}
			input.value = $(input).attr('placeholder');
			$(input).addClass('placeholder');
		}
	}
	
	var hidePlaceholder = function(input) {
		if (valueIsPlaceholder(input) && $(input).hasClass('placeholder')) {
			if (isPassword(input)) {
				try {
					input.setAttribute('type', 'password');
					// Opera loses focus when you change the type, so we have to refocus it.
					input.focus();
				} catch (e) { }
			}
			
			input.value = '';
			$(input).removeClass('placeholder');
		}
	}
	
	$(':text[placeholder],:password[placeholder],[type=email][placeholder]').each(function(index) {
		// We change the type of password fields to text so their placeholder shows.
		// We need to store somewhere that they are actually password fields so we can convert
		// back when the users types something in.
		if ($(this).attr('type') == 'password') {
			$(this).attr('realType', 'password');
		}

		showPlaceholder(this, true);
		
		$(this).focus(function() { hidePlaceholder(this) });
		$(this).blur(function() { showPlaceholder(this, false) });
	});
});