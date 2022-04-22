import xml.etree.ElementTree as ET, glob, os, random, math
from shutil import copy2

filePath = f"../dataset"
potholePath = f"{filePath}/pothole"
crackPath = f"{filePath}/crack"

def conditionCheck(path):
    if not os.path.isdir(path):
        os.mkdir(path)

trainPath = f"{filePath}/train"
testPath = f"{filePath}/test"
conditionCheck(trainPath)
conditionCheck(testPath)

trainPath += "/ready"
testPath += "/ready"
conditionCheck(trainPath)
conditionCheck(testPath)

def copy_operation(path):
    files = glob.glob(path + "/*.xml")
    random.shuffle(files)
    deviation = len(files) - 243
    if deviation != 0:
        files = files[0:-deviation]
    limit = math.ceil(len(files) * 0.8)
    index = 0
    for file in files:
        index += 1
        destPath = trainPath if index <= limit else testPath
        tree = ET.parse(file)
        root = tree.getroot()
        filename = root.find("filename").text
        nameonly, _ = os.path.splitext(filename)
        copy2(f"{path}/{filename}", f"{destPath}/{filename}")
        copy2(f"{path}/{nameonly}.xml", f"{destPath}/{nameonly}.xml")
    print("DONE\nTOTAL COPIED:", index)
    print("------------------------------\n")

copy_operation(crackPath)
copy_operation(potholePath)
