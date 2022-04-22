import xml.etree.ElementTree as ET, glob, cv2, os
from shutil import copy2

typeInput = int(input("0 for train, 1 for test\nInput: "))

print()

is_hist_equ = int(input("0 for grayscale, 1 for histogram equalization\nInput: "))
gray_or_hist = "hist_equ" if is_hist_equ else "grayscale"

type = "train" if typeInput == 0 else "test"
filePath = f"../dataset/{type}"
readyPath = f"{filePath}/ready"
histEquPath = f"{filePath}/{gray_or_hist}"

if not os.path.isdir(histEquPath):
    os.mkdir(histEquPath)

def hist_equ_image(image_path, hist_equ_path, file_name, is_hist_equ):
    gray = cv2.imread(f"{image_path}/{file_name}", flags=cv2.IMREAD_GRAYSCALE)
    gray_equ = cv2.equalizeHist(gray)
    cv2.imwrite(f"{hist_equ_path}/{file_name}", gray_equ if is_hist_equ else gray)

total = 0
for file in glob.glob(readyPath+"/*.xml"):
    tree = ET.parse(file)
    root = tree.getroot()
    file_name = root.find("filename").text
    nameonly, _ = os.path.splitext(file_name)
    copy2(f"{readyPath}/{nameonly}.xml", f"{histEquPath}/{nameonly}.xml")
    hist_equ_image(readyPath, histEquPath, file_name, is_hist_equ)
    total += 1

print(f"DONE {histEquPath}\nTotal:", total)
