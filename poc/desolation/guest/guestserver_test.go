package guest_test

import (
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/murer/desolation/guest"
	"github.com/murer/desolation/util"
	"github.com/stretchr/testify/assert"
)

func TestVersion(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(guest.Handle))
	defer server.Close()
	t.Logf("URL: %s", server.URL)
	resp, err := http.Get(server.URL + "/api/version.txt")
	util.Check(err)
	defer resp.Body.Close()
	assert.Equal(t, 200, resp.StatusCode)
	assert.Equal(t, "text/plain; charset=UTF-8", resp.Header.Get("Content-Type"))
	assert.Equal(t, util.Version, util.ReadAllString(resp.Body))
}

func TestWrite(t *testing.T) {
	server := httptest.NewServer(http.HandlerFunc(guest.Handle))
	defer server.Close()
	t.Logf("URL: %s", server.URL)

	// msg := &message.Message{
	// 	Name:    "echo",
	// 	Headers: map[string]string{"foo": "1", "bar": "2"},
	// 	Payload: "bXVyZXI=",
	// }
}
