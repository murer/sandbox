
function init(Client) {

	function msg(dest, msg) {
		this.fire('protocol_send', 'PRIVMSG ' + dest + ' :' + msg);
	}

	Client.fn.msg = msg;


}

module.exports = init;