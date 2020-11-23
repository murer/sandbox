package poc_qrcode

import "os/exec"

// zbarimg --raw poc/poc_qrcode/sample_qrcode.png  2>/dev/null | xxd

func Parse() string {
	out, err := exec.Command("zbarimg", "--raw", "sample_qrcode.png").Output()
	if err != nil {
		panic(err)
	}
	return string(out)
}
