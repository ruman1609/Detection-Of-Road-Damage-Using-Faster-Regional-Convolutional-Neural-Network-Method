import cv2, os
from tkinter.filedialog import askdirectory as explorer
from tkinter import Tk

choose_folder = Tk()
choose_folder.withdraw()
folder = explorer(parent=choose_folder,
                  title="Choose Folder with Images", initialdir="./")
choose_folder.destroy()

files = os.listdir(folder)

def preprocessing(filename, option, type):
    image = cv2.imread(filename, flags=cv2.IMREAD_COLOR)
    image = cv2.resize(image, [512, 512])
    train = "train" if option else "test"
    cv2.imwrite("../dataset/{}/{}/{}".format(train, type, filename.rsplit("/", 1)[1]),
                image)

option = input("1 for train, 0 for test\n")
option = True if option == "1" else False
type = input("1 for pothole, 2 for crack, 3 for plain\n")
if type == "1":
    type = "pothole"
elif type == "2":
    type = "crack"
else:
    type = "plain"

for file in files:
    preprocessing("{}/{}".format(folder, file), option, type)

print("DONE for {} images".format(len(files)))
