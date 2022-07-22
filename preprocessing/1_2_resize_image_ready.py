import cv2, os, glob, xml.etree.ElementTree as ET

type = int(input("1. Kaggle\n2. Ready\nPilih: "))

folder = f"../dataset/{'kaggle_pothole' if type == 1 else 'pothole_512'}"
dest = "../dataset/pothole"

if not os.path.isdir(dest):
    os.mkdir(dest)

def file_make(image, root, dest):
    image_name = root.find("filename").text
    name_only, _ = os.path.splitext(image_name)
    tree = ET.ElementTree(root)

    cv2.imwrite(os.path.join(dest, image_name), image)
    tree.write(os.path.join(dest, f"{name_only}.xml"))

def preprocessing(file, folder, dest, total):
    root = ET.parse(file).getroot()
    image = cv2.imread(os.path.join(folder, root.find("filename").text))
    height, width, _ = image.shape
    if height >= 300 and width >= 300:
        height_dest, width_dest = (300, 300)
        for member in root.findall("object"):
            x1, y1, x2, y2 = [int(data.text) for data in member.find("bndbox")]
            bbx = member.find("bndbox")
            bbx.find("ymin").text = f"{int(y1 / height * height_dest)}"
            bbx.find("ymax").text = f"{int(y2 / height * height_dest)}"
            bbx.find("xmin").text = f"{int(x1 / width * width_dest)}"
            bbx.find("xmax").text = f"{int(x2 / width * width_dest)}"
        resize = cv2.resize(image, (width_dest, height_dest))
        file_make(resize, root, dest)
        total += 1
    return total

total = 0
for file in glob.glob(folder + "/*.xml"):
    total = preprocessing(file, folder, dest, total)

print(f"DONE {folder}\nTotal {total} images")
