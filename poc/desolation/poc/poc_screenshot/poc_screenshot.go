package poc_screenshot

// import -window root png:- > test.png

import (
	"os/exec"
)

func Screenshot() []byte {
	out, err := exec.Command("import", "-window", "root", "png:-").Output()
	if err != nil {
		panic(err)
	}
	return out
}
