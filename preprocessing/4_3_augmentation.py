import xml.etree.ElementTree as ET, glob, cv2, os
from xml.dom import minidom
from shutil import copy2

# def prettify(elem):
#     """Return a pretty-printed XML string for the Element.
#     """
#     rough_string = ET.tostring(elem, 'utf-8')
#     reparsed = minidom.parseString(rough_string)
#     return reparsed.toprettyxml(indent="  ")

imageInput = int(input("0 for not sharp,\n1 for sharpen image,\n2 for grayscale,\n3 for histogram equalization\nInput: "))

imageType = ""
if imageInput == 0:
    imageType = "ready"
elif imageInput == 1:
    imageType = "sharpen"
elif imageInput == 2:
    imageType = "grayscale"
else:
    imageType = "hist_equ"

testReadyPath = f"../dataset/test/{imageType}"
trainReadyPath = f"../dataset/train/{imageType}"

testAugmentedPath = "../dataset/test/augmented"
trainAugmentedPath = "../dataset/train/augmented"

def folderCheck(augmentedPath):
    if not os.path.isdir(augmentedPath):
        os.mkdir(augmentedPath)

folderCheck(trainAugmentedPath)
folderCheck(testAugmentedPath)

def augmentation_maker(augmented, image, typeAug, filename, augmentedPath):
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

def augmentation_flip_h(root, readyPath, augmentedPath): # Horizontal Flip
    augmented = ET.fromstring(ET.tostring(root))
    filename = augmented.find("filename").text
    image = cv2.imread(readyPath + "/" + filename)
    height, width, _ = image.shape
    flip = cv2.flip(image, 1)
    for member in augmented.findall("object"):
        x1, y1, x2, y2 = [int(data.text) for data in member.find("bndbox")]
        bbx = member.find("bndbox")
        bbx.find("xmin").text = f"{height - x2}"
        bbx.find("xmax").text = f"{height - x1}"
    augmentation_maker(augmented, flip, "horizontalFlip", filename, augmentedPath)

def augmentation_flip_v(root, readyPath, augmentedPath): # Vertical Flip
    augmented = ET.fromstring(ET.tostring(root))
    filename = augmented.find("filename").text
    image = cv2.imread(readyPath + "/" + filename)
    height, width, _ = image.shape
    flip = cv2.flip(image, 0)
    for member in augmented.findall("object"):
        x1, y1, x2, y2 = [int(data.text) for data in member.find("bndbox")]
        bbx = member.find("bndbox")
        bbx.find("ymin").text = f"{width - y2}"
        bbx.find("ymax").text = f"{width - y1}"
    augmentation_maker(augmented, flip, "verticalFlip", filename, augmentedPath)

def augmentation_rotate_90r(root, readyPath, augmentedPath): # Rotate 90 Right
    augmented = ET.fromstring(ET.tostring(root))
    filename = augmented.find("filename").text
    image = cv2.imread(readyPath + "/" + filename)
    height, width, _ = image.shape
    rotate = cv2.rotate(image, cv2.ROTATE_90_CLOCKWISE)
    for member in augmented.findall("object"):
        x1, y1, x2, y2 = [int(data.text) for data in member.find("bndbox")]
        bbx = member.find("bndbox")
        bbx.find("xmin").text = f"{width - y2}"
        bbx.find("xmax").text = f"{width - y1}"
        bbx.find("ymin").text = f"{x1}"
        bbx.find("ymax").text = f"{x2}"
    augmentation_maker(augmented, rotate, "90rRotate", filename, augmentedPath)

def augmentation_rotate_90l(root, readyPath, augmentedPath): # Rotate 90 Left
    augmented = ET.fromstring(ET.tostring(root))
    filename = augmented.find("filename").text
    image = cv2.imread(readyPath + "/" + filename)
    height, width, _ = image.shape
    rotate = cv2.rotate(image, cv2.ROTATE_90_COUNTERCLOCKWISE)
    for member in augmented.findall("object"):
        x1, y1, x2, y2 = [int(data.text) for data in member.find("bndbox")]
        bbx = member.find("bndbox")
        bbx.find("ymin").text = f"{height - x2}"
        bbx.find("ymax").text = f"{height - x1}"
        bbx.find("xmin").text = f"{y1}"
        bbx.find("xmax").text = f"{y2}"
    augmentation_maker(augmented, rotate, "90lRotate", filename, augmentedPath)

def augmenting_process(readyPath, augmentedPath, is_augmenting = True):
    for file in glob.glob(readyPath + "/*.xml"):
        tree = ET.parse(file)
        root = tree.getroot()

        filename = root.find("filename").text
        nameonly, _ = os.path.splitext(filename)

        if is_augmenting:
            augmentation_flip_h(root, readyPath, augmentedPath)
            augmentation_flip_v(root, readyPath, augmentedPath)
            augmentation_rotate_90r(root, readyPath, augmentedPath)
            augmentation_rotate_90l(root, readyPath, augmentedPath)

        copy2(f"{readyPath}/{filename}", f"{augmentedPath}/{filename}")
        copy2(f"{readyPath}/{nameonly}.xml", f"{augmentedPath}/{nameonly}.xml")
    print(f"Creating Augmentation at {augmentedPath} done!")

augmenting_process(trainReadyPath, trainAugmentedPath)
augmenting_process(testReadyPath, testAugmentedPath, False)


"""
# how to make bounding box
cv2.rectangle(image, (x1, y1), (x2, y2), (255, 0, 0), 2)
cv2.imshow("Asli", image)
cv2.waitKey(0)
"""
