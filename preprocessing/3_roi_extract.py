import xml.etree.ElementTree as ET, pandas as pd, numpy as np, glob, os, random, math, cv2
from shutil import copy2

filePath = f"../dataset"
potholePath = f"{filePath}/pothole"
crackPath = f"{filePath}/crack"

def conditionCheck(path):
    if not os.path.isdir(path):
        os.mkdir(path)

trainPath = f"{filePath}/train_roi"
testPath = f"{filePath}/test_roi"
conditionCheck(trainPath)
conditionCheck(testPath)

trainPath += "/ready"
testPath += "/ready"
conditionCheck(trainPath)
conditionCheck(testPath)

def resize_roi(image_path, dest_path, root, data, counter):
    image = cv2.imread(image_path, flags=cv2.IMREAD_COLOR)
    # image = cv2.imread(image_path, flag=cv2.IMREAD_GRAYSCALE)

    for member in root.findall("object"):
        x1, y1, x2, y2 = [int(data.text) for data in member.find("bndbox")]
        width = x2 - x1
        height = y2 - y1
        object = member.find("name").text
        if width < 64 or height < 64 or object == "plain":
            continue

        roi = image[y1:y2, x1:x2]  # height, width
        roi = cv2.resize(roi, [64, 64])
        counter += 1
        filename = f"{object}_{counter}.jpg"
        data.append({
            "filename": filename,
            "class": object
        })
        cv2.imwrite(f"{dest_path}/{filename}", roi)
    return counter

def create_csv(data, dest, type):
    data = pd.DataFrame(list(data))
    data.to_csv(f"{dest}/{type}.csv", index=False)

def roi_extract(path, counter, train_data, test_data):
    files = glob.glob(path + "/*.xml")
    random.shuffle(files)
    limit = math.ceil(300 * 0.8)
    total = 0
    data = []
    for file in files:
        total += 1
        dest = trainPath if total <= limit else testPath
        if (total > 300):
            test_data = np.append(test_data, data)
            data = []
            break

        tree = ET.parse(file)
        root = tree.getroot()
        filename = root.find("filename").text
        counter = resize_roi(f"{path}/{filename}", dest, root, data, counter)
        if total == limit:
            train_data = np.append(train_data, data)
            data = []
    print(f"DONE {path}")
    return counter, train_data, test_data

counter = 0
train_data = np.array([])
test_data = np.array([])

counter, train_data, test_data = roi_extract(crackPath, counter, train_data, test_data)
counter, train_data, test_data = roi_extract(potholePath, counter, train_data, test_data)

create_csv(train_data, trainPath, "train")
create_csv(test_data, testPath, "test")
