import xml.etree.ElementTree as ET, glob, cv2, os
from xml.dom import minidom
from shutil import copy2

def prettify(elem):
    """Return a pretty-printed XML string for the Element.
    """
    rough_string = ET.tostring(elem, 'utf-8')
    reparsed = minidom.parseString(rough_string)
    return reparsed.toprettyxml(indent="  ")

typeInput = int(input("0 for train, 1 for test\nInput: "))

type = "train" if typeInput == 0 else "test"

filePath = f"../dataset/{type}"
readyPath = f"{filePath}/ready"
augmentedPath = f"{filePath}/augmented"
if not os.path.isdir(augmentedPath):
    os.mkdir(augmentedPath)

def augmentation_maker(augmented, image, typeAug, filename):

    nameonly, extension = os.path.splitext(filename)
    imagename = f"{nameonly}_{typeAug}{extension}"
    xmlname = f"{nameonly}_{typeAug}.xml"
    augmented.find("filename").text = f"{nameonly}_{typeAug}{extension}"
    tree = ET.ElementTree(augmented)
    tree.write(f"{augmentedPath}/{xmlname}")
    cv2.imwrite(f"{augmentedPath}/{imagename}", image)

    # for member in augmented.findall("object"):
    #     x1, y1, x2, y2 = [int(data.text) for data in member.find("bndbox")]
    #     cv2.rectangle(image, (x1, y1), (x2, y2), (255, 0, 0), 2)
    #     cv2.imshow("Augmented", image)
    #     cv2.waitKey(0)
    #     break

def augmentation_flip_h(root, x1, y1, x2, y2, width, height): # Horizontal Flip
    augmented = ET.fromstring(ET.tostring(root))
    filename = augmented.find("filename").text
    for member in augmented.findall("object"):
        bbx = member.find("bndbox")
        bbx.find("xmin").text = f"{height - x2}"
        bbx.find("xmax").text = f"{height - x1}"
    image = cv2.imread(readyPath + "/" + filename)
    flip = cv2.flip(image, 1)
    augmentation_maker(augmented, flip, "horizontalFlip", filename)

def augmentation_flip_v(root, x1, y1, x2, y2, width, height): # Vertical Flip
    augmented = ET.fromstring(ET.tostring(root))
    filename = augmented.find("filename").text
    for member in augmented.findall("object"):
        bbx = member.find("bndbox")
        bbx.find("ymin").text = f"{width - y2}"
        bbx.find("ymax").text = f"{width - y1}"
    image = cv2.imread(readyPath + "/" + filename)
    flip = cv2.flip(image, 0)
    augmentation_maker(augmented, flip, "verticalFlip", filename)

def augmentation_rotate_90r(root, x1, y1, x2, y2, width, height): # Rotate 90 Right
    augmented = ET.fromstring(ET.tostring(root))
    filename = augmented.find("filename").text
    for member in augmented.findall("object"):
        bbx = member.find("bndbox")
        bbx.find("xmin").text = f"{width - y2}"
        bbx.find("xmax").text = f"{width - y1}"
        bbx.find("ymin").text = f"{x1}"
        bbx.find("ymax").text = f"{x2}"
    image = cv2.imread(readyPath + "/" + filename)
    rotate = cv2.rotate(image, cv2.ROTATE_90_CLOCKWISE)
    augmentation_maker(augmented, rotate, "90rRotate", filename)

def augmentation_rotate_90l(root, x1, y1, x2, y2, width, height): # Rotate 90 Left
    augmented = ET.fromstring(ET.tostring(root))
    filename = augmented.find("filename").text
    for member in augmented.findall("object"):
        bbx = member.find("bndbox")
        bbx.find("ymin").text = f"{height - x2}"
        bbx.find("ymax").text = f"{height - x1}"
        bbx.find("xmin").text = f"{y1}"
        bbx.find("xmax").text = f"{y2}"
    image = cv2.imread(readyPath + "/" + filename)
    rotate = cv2.rotate(image, cv2.ROTATE_90_COUNTERCLOCKWISE)
    augmentation_maker(augmented, rotate, "90lRotate", filename)

for file in glob.glob(readyPath + "/*.xml"):
    tree = ET.parse(file)
    root = tree.getroot()
    width, height, _ = [int(data.text) for data in root.find("size")]
    for member in root.findall("object"):
        x1, y1, x2, y2 = [int(data.text) for data in member.find("bndbox")]
    augmentation_flip_h(root, x1, y1, x2, y2, width, height)
    augmentation_flip_v(root, x1, y1, x2, y2, width, height)
    augmentation_rotate_90r(root, x1, y1, x2, y2, width, height)
    augmentation_rotate_90l(root, x1, y1, x2, y2, width, height)

    filename = root.find("filename").text
    nameonly, _ = os.path.splitext(filename)
    copy2(f"{readyPath}/{filename}", f"{augmentedPath}/{filename}")
    copy2(f"{readyPath}/{nameonly}.xml", f"{augmentedPath}/{nameonly}.xml")
print(f"Creating Augmentation at {augmentedPath} done!")


"""
# how to make bounding box
cv2.rectangle(image, (x1, y1), (x2, y2), (255, 0, 0), 2)
cv2.imshow("Asli", image)
cv2.waitKey(0)
"""
