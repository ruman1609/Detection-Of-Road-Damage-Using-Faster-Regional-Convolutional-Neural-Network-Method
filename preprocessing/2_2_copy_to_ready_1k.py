import xml.etree.ElementTree as ET, json, os, random, math
from shutil import copy2

filePath = f"../dataset"
potholePath = f"{filePath}/pothole"
split_json_path = f"{filePath}/kaggle_pothole_1k/splits.json"

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

def copy_operation(path, destPath, files, total):
    for file in files:
        tree = ET.parse(f"{path}/{file}")
        root = tree.getroot()
        filename = root.find("filename").text
        nameonly, _ = os.path.splitext(filename)
        copy2(f"{path}/{filename}", f"{destPath}/{filename}")
        copy2(f"{path}/{nameonly}.xml", f"{destPath}/{nameonly}.xml")
        total += 1
    return total

f = open(split_json_path)
data = json.load(f)
f.close()

total = 0
total = copy_operation(potholePath, trainPath, data["train"], total)
total = copy_operation(potholePath, testPath, data["test"], total)
print("DONE\nTOTAL COPIED:", total)
print("------------------------------\n")
