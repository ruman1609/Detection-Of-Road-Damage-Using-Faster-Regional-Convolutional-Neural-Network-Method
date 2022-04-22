import xml.etree.ElementTree as ET, numpy as np, glob, cv2, os
from shutil import copy2

typeInput = int(input("0 for train, 1 for test\nInput: "))

type = "train" if typeInput == 0 else "test"
filePath = f"../dataset/{type}"
readyPath = f"{filePath}/ready"
sharpenPath = f"{filePath}/sharpen"

if not os.path.isdir(sharpenPath):
    os.mkdir(sharpenPath)

def sharpening_image(image_path, sharpen_path, file_name):
    image = cv2.imread(f"{image_path}/{file_name}", flags=cv2.IMREAD_COLOR)
    kernel = np.array([[0, -1, 0],
                   [-1, 5,-1],
                   [0, -1, 0]])
    image_sharp = cv2.filter2D(src=image, ddepth=-1, kernel=kernel)
    cv2.imwrite(f"{sharpen_path}/{file_name}", image_sharp)

total = 0
for file in glob.glob(readyPath+"/*.xml"):
    tree = ET.parse(file)
    root = tree.getroot()
    file_name = root.find("filename").text
    nameonly, _ = os.path.splitext(file_name)
    copy2(f"{readyPath}/{nameonly}.xml", f"{sharpenPath}/{nameonly}.xml")
    sharpening_image(readyPath, sharpenPath, file_name)
    total += 1

print("DONE\nTotal:", total)
