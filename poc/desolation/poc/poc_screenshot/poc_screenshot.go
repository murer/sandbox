package poc_screenshot

// import -window root png:- > test.png

import (
	"fmt"
	"image/png"
	"os"

	"github.com/kbinani/screenshot"
)

func Noop() int {
	n := screenshot.NumActiveDisplays()
	for i := 0; i < n; i++ {
		img, err := screenshot.CaptureDisplay(i)
		if err != nil {
			panic(err)
		}
		f, err := os.Create(fmt.Sprintf("/tmp/m-%d.png", i))
		if err != nil {
			panic(err)
		}
		defer f.Close()
		png.Encode(f, img)
	}
	return n
}
