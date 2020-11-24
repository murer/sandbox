package guest

import (
	"log"
	"net/http"
	"strings"

	"github.com/murer/desolation/util"
)

func Start() {
	http.HandleFunc("/", Handle)
	log.Printf("Starting server")
	err := http.ListenAndServe("localhost:0", nil)
	util.Check(err)
}

func Handle(w http.ResponseWriter, r *http.Request) {
	log.Printf("Access: %s %s %s", r.RemoteAddr, r.Method, r.URL)
	if r.Method == "GET" && r.URL.Path == "/api/version.txt" {
		util.RespText(w, util.Version)
	} else if strings.HasPrefix(r.URL.Path, "/self/") {
	} else if r.Method == "POST" {
	} else {
		http.NotFound(w, r)
	}
}
