# Pothole Faster R-CNN Detection (Android Code also Available)

## Python

This is tensorflow Faster-RCNN implementation from scratch supporting to the batch processing.
All methods are tried to be created in the simplest way for easy understanding.
Most of the operations performed during the implementation were carried out as described in the [paper](https://arxiv.org/abs/1506.01497) and [tf-rpn](https://github.com/FurkanOM/tf-rpn) repository.

It's implemented and tested with **tensorflow 2.8**, and **Python 3.7.9**.

[VGG-16](https://www.tensorflow.org/api_docs/python/tf/keras/applications/VGG16), [ResNet-50]  (https://www.tensorflow.org/api_docs/python/tf/keras/applications/resnet50/ResNet50), and [MobilenetV2](https://www.tensorflow.org/api_docs/python/tf/keras/applications/mobilenet_v2/MobileNetV2) are supported in this project

This project use 300&times;300 size for input. Please change the feature_map_shape according to your input shape size.

Check [train_utils.py](framework/utils/train_utils.py) to change feature_map_shape or input shape size (img_size)

To check value for feature_map_shape you can run python in terminal and get the summary, to make it simpler I'll show you how to do it.

### VGG16
```python
from tensorflow.keras.applications.vgg16 import VGG16
a = VGG16(include_top = False, input_shape = [300, 300, 3])
a.summary()
```

And so the output will be like this
<details><summary>View Output</summary>
```
_________________________________________________________________
 Layer (type)                Output Shape              Param #
=================================================================
 input_1 (InputLayer)        [(None, 300, 300, 3)]     0

 block1_conv1 (Conv2D)       (None, 300, 300, 64)      1792

 block1_conv2 (Conv2D)       (None, 300, 300, 64)      36928

 block1_pool (MaxPooling2D)  (None, 150, 150, 64)      0

 block2_conv1 (Conv2D)       (None, 150, 150, 128)     73856

 block2_conv2 (Conv2D)       (None, 150, 150, 128)     147584

 block2_pool (MaxPooling2D)  (None, 75, 75, 128)       0

 block3_conv1 (Conv2D)       (None, 75, 75, 256)       295168

 block3_conv2 (Conv2D)       (None, 75, 75, 256)       590080

 block3_conv3 (Conv2D)       (None, 75, 75, 256)       590080

 block3_pool (MaxPooling2D)  (None, 37, 37, 256)       0

 block4_conv1 (Conv2D)       (None, 37, 37, 512)       1180160

 block4_conv2 (Conv2D)       (None, 37, 37, 512)       2359808

 block4_conv3 (Conv2D)       (None, 37, 37, 512)       2359808

 block4_pool (MaxPooling2D)  (None, 18, 18, 512)       0

 block5_conv1 (Conv2D)       (None, 18, 18, 512)       2359808

 block5_conv2 (Conv2D)       (None, 18, 18, 512)       2359808

 block5_conv3 (Conv2D)       (None, 18, 18, 512)       2359808

 block5_pool (MaxPooling2D)  (None, 9, 9, 512)         0

=================================================================
Total params: 14,714,688
Trainable params: 14,714,688
Non-trainable params: 0
_________________________________________________________________
```
</details>
for feature_map_shape valu can be seen in `block5_conv3` which is 18

### ResNet50
```python
from tensorflow.keras.applications.resnet50 import ResNet50
a = ResNet50(include_top = False, input_shape = [300, 300, 3])
a.summary()
```

And so the output will be like this
<details><summary>View Output</summary>
```
__________________________________________________________________________________________________
 Layer (type)                   Output Shape         Param #     Connected to
==================================================================================================
 input_2 (InputLayer)           [(None, 300, 300, 3  0           []
                                )]

 conv1_pad (ZeroPadding2D)      (None, 306, 306, 3)  0           ['input_2[0][0]']

 conv1_conv (Conv2D)            (None, 150, 150, 64  9472        ['conv1_pad[0][0]']
                                )

 conv1_bn (BatchNormalization)  (None, 150, 150, 64  256         ['conv1_conv[0][0]']
                                )

 conv1_relu (Activation)        (None, 150, 150, 64  0           ['conv1_bn[0][0]']
                                )

 pool1_pad (ZeroPadding2D)      (None, 152, 152, 64  0           ['conv1_relu[0][0]']
                                )

 pool1_pool (MaxPooling2D)      (None, 75, 75, 64)   0           ['pool1_pad[0][0]']

 conv2_block1_1_conv (Conv2D)   (None, 75, 75, 64)   4160        ['pool1_pool[0][0]']

 conv2_block1_1_bn (BatchNormal  (None, 75, 75, 64)  256         ['conv2_block1_1_conv[0][0]']
 ization)

 conv2_block1_1_relu (Activatio  (None, 75, 75, 64)  0           ['conv2_block1_1_bn[0][0]']
 n)

 conv2_block1_2_conv (Conv2D)   (None, 75, 75, 64)   36928       ['conv2_block1_1_relu[0][0]']

 conv2_block1_2_bn (BatchNormal  (None, 75, 75, 64)  256         ['conv2_block1_2_conv[0][0]']
 ization)

 conv2_block1_2_relu (Activatio  (None, 75, 75, 64)  0           ['conv2_block1_2_bn[0][0]']
 n)

 conv2_block1_0_conv (Conv2D)   (None, 75, 75, 256)  16640       ['pool1_pool[0][0]']

 conv2_block1_3_conv (Conv2D)   (None, 75, 75, 256)  16640       ['conv2_block1_2_relu[0][0]']

 conv2_block1_0_bn (BatchNormal  (None, 75, 75, 256)  1024       ['conv2_block1_0_conv[0][0]']
 ization)

 conv2_block1_3_bn (BatchNormal  (None, 75, 75, 256)  1024       ['conv2_block1_3_conv[0][0]']
 ization)

 conv2_block1_add (Add)         (None, 75, 75, 256)  0           ['conv2_block1_0_bn[0][0]',
                                                                  'conv2_block1_3_bn[0][0]']

 conv2_block1_out (Activation)  (None, 75, 75, 256)  0           ['conv2_block1_add[0][0]']

 conv2_block2_1_conv (Conv2D)   (None, 75, 75, 64)   16448       ['conv2_block1_out[0][0]']

 conv2_block2_1_bn (BatchNormal  (None, 75, 75, 64)  256         ['conv2_block2_1_conv[0][0]']
 ization)

 conv2_block2_1_relu (Activatio  (None, 75, 75, 64)  0           ['conv2_block2_1_bn[0][0]']
 n)

 conv2_block2_2_conv (Conv2D)   (None, 75, 75, 64)   36928       ['conv2_block2_1_relu[0][0]']

 conv2_block2_2_bn (BatchNormal  (None, 75, 75, 64)  256         ['conv2_block2_2_conv[0][0]']
 ization)

 conv2_block2_2_relu (Activatio  (None, 75, 75, 64)  0           ['conv2_block2_2_bn[0][0]']
 n)

 conv2_block2_3_conv (Conv2D)   (None, 75, 75, 256)  16640       ['conv2_block2_2_relu[0][0]']

 conv2_block2_3_bn (BatchNormal  (None, 75, 75, 256)  1024       ['conv2_block2_3_conv[0][0]']
 ization)

 conv2_block2_add (Add)         (None, 75, 75, 256)  0           ['conv2_block1_out[0][0]',
                                                                  'conv2_block2_3_bn[0][0]']

 conv2_block2_out (Activation)  (None, 75, 75, 256)  0           ['conv2_block2_add[0][0]']

 conv2_block3_1_conv (Conv2D)   (None, 75, 75, 64)   16448       ['conv2_block2_out[0][0]']

 conv2_block3_1_bn (BatchNormal  (None, 75, 75, 64)  256         ['conv2_block3_1_conv[0][0]']
 ization)

 conv2_block3_1_relu (Activatio  (None, 75, 75, 64)  0           ['conv2_block3_1_bn[0][0]']
 n)

 conv2_block3_2_conv (Conv2D)   (None, 75, 75, 64)   36928       ['conv2_block3_1_relu[0][0]']

 conv2_block3_2_bn (BatchNormal  (None, 75, 75, 64)  256         ['conv2_block3_2_conv[0][0]']
 ization)

 conv2_block3_2_relu (Activatio  (None, 75, 75, 64)  0           ['conv2_block3_2_bn[0][0]']
 n)

 conv2_block3_3_conv (Conv2D)   (None, 75, 75, 256)  16640       ['conv2_block3_2_relu[0][0]']

 conv2_block3_3_bn (BatchNormal  (None, 75, 75, 256)  1024       ['conv2_block3_3_conv[0][0]']
 ization)

 conv2_block3_add (Add)         (None, 75, 75, 256)  0           ['conv2_block2_out[0][0]',
                                                                  'conv2_block3_3_bn[0][0]']

 conv2_block3_out (Activation)  (None, 75, 75, 256)  0           ['conv2_block3_add[0][0]']

 conv3_block1_1_conv (Conv2D)   (None, 38, 38, 128)  32896       ['conv2_block3_out[0][0]']

 conv3_block1_1_bn (BatchNormal  (None, 38, 38, 128)  512        ['conv3_block1_1_conv[0][0]']
 ization)

 conv3_block1_1_relu (Activatio  (None, 38, 38, 128)  0          ['conv3_block1_1_bn[0][0]']
 n)

 conv3_block1_2_conv (Conv2D)   (None, 38, 38, 128)  147584      ['conv3_block1_1_relu[0][0]']

 conv3_block1_2_bn (BatchNormal  (None, 38, 38, 128)  512        ['conv3_block1_2_conv[0][0]']
 ization)

 conv3_block1_2_relu (Activatio  (None, 38, 38, 128)  0          ['conv3_block1_2_bn[0][0]']
 n)

 conv3_block1_0_conv (Conv2D)   (None, 38, 38, 512)  131584      ['conv2_block3_out[0][0]']

 conv3_block1_3_conv (Conv2D)   (None, 38, 38, 512)  66048       ['conv3_block1_2_relu[0][0]']

 conv3_block1_0_bn (BatchNormal  (None, 38, 38, 512)  2048       ['conv3_block1_0_conv[0][0]']
 ization)

 conv3_block1_3_bn (BatchNormal  (None, 38, 38, 512)  2048       ['conv3_block1_3_conv[0][0]']
 ization)

 conv3_block1_add (Add)         (None, 38, 38, 512)  0           ['conv3_block1_0_bn[0][0]',
                                                                  'conv3_block1_3_bn[0][0]']

 conv3_block1_out (Activation)  (None, 38, 38, 512)  0           ['conv3_block1_add[0][0]']

 conv3_block2_1_conv (Conv2D)   (None, 38, 38, 128)  65664       ['conv3_block1_out[0][0]']

 conv3_block2_1_bn (BatchNormal  (None, 38, 38, 128)  512        ['conv3_block2_1_conv[0][0]']
 ization)

 conv3_block2_1_relu (Activatio  (None, 38, 38, 128)  0          ['conv3_block2_1_bn[0][0]']
 n)

 conv3_block2_2_conv (Conv2D)   (None, 38, 38, 128)  147584      ['conv3_block2_1_relu[0][0]']

 conv3_block2_2_bn (BatchNormal  (None, 38, 38, 128)  512        ['conv3_block2_2_conv[0][0]']
 ization)

 conv3_block2_2_relu (Activatio  (None, 38, 38, 128)  0          ['conv3_block2_2_bn[0][0]']
 n)

 conv3_block2_3_conv (Conv2D)   (None, 38, 38, 512)  66048       ['conv3_block2_2_relu[0][0]']

 conv3_block2_3_bn (BatchNormal  (None, 38, 38, 512)  2048       ['conv3_block2_3_conv[0][0]']
 ization)

 conv3_block2_add (Add)         (None, 38, 38, 512)  0           ['conv3_block1_out[0][0]',
                                                                  'conv3_block2_3_bn[0][0]']

 conv3_block2_out (Activation)  (None, 38, 38, 512)  0           ['conv3_block2_add[0][0]']

 conv3_block3_1_conv (Conv2D)   (None, 38, 38, 128)  65664       ['conv3_block2_out[0][0]']

 conv3_block3_1_bn (BatchNormal  (None, 38, 38, 128)  512        ['conv3_block3_1_conv[0][0]']
 ization)

 conv3_block3_1_relu (Activatio  (None, 38, 38, 128)  0          ['conv3_block3_1_bn[0][0]']
 n)

 conv3_block3_2_conv (Conv2D)   (None, 38, 38, 128)  147584      ['conv3_block3_1_relu[0][0]']

 conv3_block3_2_bn (BatchNormal  (None, 38, 38, 128)  512        ['conv3_block3_2_conv[0][0]']
 ization)

 conv3_block3_2_relu (Activatio  (None, 38, 38, 128)  0          ['conv3_block3_2_bn[0][0]']
 n)

 conv3_block3_3_conv (Conv2D)   (None, 38, 38, 512)  66048       ['conv3_block3_2_relu[0][0]']

 conv3_block3_3_bn (BatchNormal  (None, 38, 38, 512)  2048       ['conv3_block3_3_conv[0][0]']
 ization)

 conv3_block3_add (Add)         (None, 38, 38, 512)  0           ['conv3_block2_out[0][0]',
                                                                  'conv3_block3_3_bn[0][0]']

 conv3_block3_out (Activation)  (None, 38, 38, 512)  0           ['conv3_block3_add[0][0]']

 conv3_block4_1_conv (Conv2D)   (None, 38, 38, 128)  65664       ['conv3_block3_out[0][0]']

 conv3_block4_1_bn (BatchNormal  (None, 38, 38, 128)  512        ['conv3_block4_1_conv[0][0]']
 ization)

 conv3_block4_1_relu (Activatio  (None, 38, 38, 128)  0          ['conv3_block4_1_bn[0][0]']
 n)

 conv3_block4_2_conv (Conv2D)   (None, 38, 38, 128)  147584      ['conv3_block4_1_relu[0][0]']

 conv3_block4_2_bn (BatchNormal  (None, 38, 38, 128)  512        ['conv3_block4_2_conv[0][0]']
 ization)

 conv3_block4_2_relu (Activatio  (None, 38, 38, 128)  0          ['conv3_block4_2_bn[0][0]']
 n)

 conv3_block4_3_conv (Conv2D)   (None, 38, 38, 512)  66048       ['conv3_block4_2_relu[0][0]']

 conv3_block4_3_bn (BatchNormal  (None, 38, 38, 512)  2048       ['conv3_block4_3_conv[0][0]']
 ization)

 conv3_block4_add (Add)         (None, 38, 38, 512)  0           ['conv3_block3_out[0][0]',
                                                                  'conv3_block4_3_bn[0][0]']

 conv3_block4_out (Activation)  (None, 38, 38, 512)  0           ['conv3_block4_add[0][0]']

 conv4_block1_1_conv (Conv2D)   (None, 19, 19, 256)  131328      ['conv3_block4_out[0][0]']

 conv4_block1_1_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block1_1_conv[0][0]']
 ization)

 conv4_block1_1_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block1_1_bn[0][0]']
 n)

 conv4_block1_2_conv (Conv2D)   (None, 19, 19, 256)  590080      ['conv4_block1_1_relu[0][0]']

 conv4_block1_2_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block1_2_conv[0][0]']
 ization)

 conv4_block1_2_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block1_2_bn[0][0]']
 n)

 conv4_block1_0_conv (Conv2D)   (None, 19, 19, 1024  525312      ['conv3_block4_out[0][0]']
                                )

 conv4_block1_3_conv (Conv2D)   (None, 19, 19, 1024  263168      ['conv4_block1_2_relu[0][0]']
                                )

 conv4_block1_0_bn (BatchNormal  (None, 19, 19, 1024  4096       ['conv4_block1_0_conv[0][0]']
 ization)                       )

 conv4_block1_3_bn (BatchNormal  (None, 19, 19, 1024  4096       ['conv4_block1_3_conv[0][0]']
 ization)                       )

 conv4_block1_add (Add)         (None, 19, 19, 1024  0           ['conv4_block1_0_bn[0][0]',
                                )                                 'conv4_block1_3_bn[0][0]']

 conv4_block1_out (Activation)  (None, 19, 19, 1024  0           ['conv4_block1_add[0][0]']
                                )

 conv4_block2_1_conv (Conv2D)   (None, 19, 19, 256)  262400      ['conv4_block1_out[0][0]']

 conv4_block2_1_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block2_1_conv[0][0]']
 ization)

 conv4_block2_1_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block2_1_bn[0][0]']
 n)

 conv4_block2_2_conv (Conv2D)   (None, 19, 19, 256)  590080      ['conv4_block2_1_relu[0][0]']

 conv4_block2_2_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block2_2_conv[0][0]']
 ization)

 conv4_block2_2_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block2_2_bn[0][0]']
 n)

 conv4_block2_3_conv (Conv2D)   (None, 19, 19, 1024  263168      ['conv4_block2_2_relu[0][0]']
                                )

 conv4_block2_3_bn (BatchNormal  (None, 19, 19, 1024  4096       ['conv4_block2_3_conv[0][0]']
 ization)                       )

 conv4_block2_add (Add)         (None, 19, 19, 1024  0           ['conv4_block1_out[0][0]',
                                )                                 'conv4_block2_3_bn[0][0]']

 conv4_block2_out (Activation)  (None, 19, 19, 1024  0           ['conv4_block2_add[0][0]']
                                )

 conv4_block3_1_conv (Conv2D)   (None, 19, 19, 256)  262400      ['conv4_block2_out[0][0]']

 conv4_block3_1_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block3_1_conv[0][0]']
 ization)

 conv4_block3_1_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block3_1_bn[0][0]']
 n)

 conv4_block3_2_conv (Conv2D)   (None, 19, 19, 256)  590080      ['conv4_block3_1_relu[0][0]']

 conv4_block3_2_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block3_2_conv[0][0]']
 ization)

 conv4_block3_2_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block3_2_bn[0][0]']
 n)

 conv4_block3_3_conv (Conv2D)   (None, 19, 19, 1024  263168      ['conv4_block3_2_relu[0][0]']
                                )

 conv4_block3_3_bn (BatchNormal  (None, 19, 19, 1024  4096       ['conv4_block3_3_conv[0][0]']
 ization)                       )

 conv4_block3_add (Add)         (None, 19, 19, 1024  0           ['conv4_block2_out[0][0]',
                                )                                 'conv4_block3_3_bn[0][0]']

 conv4_block3_out (Activation)  (None, 19, 19, 1024  0           ['conv4_block3_add[0][0]']
                                )

 conv4_block4_1_conv (Conv2D)   (None, 19, 19, 256)  262400      ['conv4_block3_out[0][0]']

 conv4_block4_1_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block4_1_conv[0][0]']
 ization)

 conv4_block4_1_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block4_1_bn[0][0]']
 n)

 conv4_block4_2_conv (Conv2D)   (None, 19, 19, 256)  590080      ['conv4_block4_1_relu[0][0]']

 conv4_block4_2_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block4_2_conv[0][0]']
 ization)

 conv4_block4_2_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block4_2_bn[0][0]']
 n)

 conv4_block4_3_conv (Conv2D)   (None, 19, 19, 1024  263168      ['conv4_block4_2_relu[0][0]']
                                )

 conv4_block4_3_bn (BatchNormal  (None, 19, 19, 1024  4096       ['conv4_block4_3_conv[0][0]']
 ization)                       )

 conv4_block4_add (Add)         (None, 19, 19, 1024  0           ['conv4_block3_out[0][0]',
                                )                                 'conv4_block4_3_bn[0][0]']

 conv4_block4_out (Activation)  (None, 19, 19, 1024  0           ['conv4_block4_add[0][0]']
                                )

 conv4_block5_1_conv (Conv2D)   (None, 19, 19, 256)  262400      ['conv4_block4_out[0][0]']

 conv4_block5_1_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block5_1_conv[0][0]']
 ization)

 conv4_block5_1_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block5_1_bn[0][0]']
 n)

 conv4_block5_2_conv (Conv2D)   (None, 19, 19, 256)  590080      ['conv4_block5_1_relu[0][0]']

 conv4_block5_2_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block5_2_conv[0][0]']
 ization)

 conv4_block5_2_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block5_2_bn[0][0]']
 n)

 conv4_block5_3_conv (Conv2D)   (None, 19, 19, 1024  263168      ['conv4_block5_2_relu[0][0]']
                                )

 conv4_block5_3_bn (BatchNormal  (None, 19, 19, 1024  4096       ['conv4_block5_3_conv[0][0]']
 ization)                       )

 conv4_block5_add (Add)         (None, 19, 19, 1024  0           ['conv4_block4_out[0][0]',
                                )                                 'conv4_block5_3_bn[0][0]']

 conv4_block5_out (Activation)  (None, 19, 19, 1024  0           ['conv4_block5_add[0][0]']
                                )

 conv4_block6_1_conv (Conv2D)   (None, 19, 19, 256)  262400      ['conv4_block5_out[0][0]']

 conv4_block6_1_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block6_1_conv[0][0]']
 ization)

 conv4_block6_1_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block6_1_bn[0][0]']
 n)

 conv4_block6_2_conv (Conv2D)   (None, 19, 19, 256)  590080      ['conv4_block6_1_relu[0][0]']

 conv4_block6_2_bn (BatchNormal  (None, 19, 19, 256)  1024       ['conv4_block6_2_conv[0][0]']
 ization)

 conv4_block6_2_relu (Activatio  (None, 19, 19, 256)  0          ['conv4_block6_2_bn[0][0]']
 n)

 conv4_block6_3_conv (Conv2D)   (None, 19, 19, 1024  263168      ['conv4_block6_2_relu[0][0]']
                                )

 conv4_block6_3_bn (BatchNormal  (None, 19, 19, 1024  4096       ['conv4_block6_3_conv[0][0]']
 ization)                       )

 conv4_block6_add (Add)         (None, 19, 19, 1024  0           ['conv4_block5_out[0][0]',
                                )                                 'conv4_block6_3_bn[0][0]']

 conv4_block6_out (Activation)  (None, 19, 19, 1024  0           ['conv4_block6_add[0][0]']
                                )

 conv5_block1_1_conv (Conv2D)   (None, 10, 10, 512)  524800      ['conv4_block6_out[0][0]']

 conv5_block1_1_bn (BatchNormal  (None, 10, 10, 512)  2048       ['conv5_block1_1_conv[0][0]']
 ization)

 conv5_block1_1_relu (Activatio  (None, 10, 10, 512)  0          ['conv5_block1_1_bn[0][0]']
 n)

 conv5_block1_2_conv (Conv2D)   (None, 10, 10, 512)  2359808     ['conv5_block1_1_relu[0][0]']

 conv5_block1_2_bn (BatchNormal  (None, 10, 10, 512)  2048       ['conv5_block1_2_conv[0][0]']
 ization)

 conv5_block1_2_relu (Activatio  (None, 10, 10, 512)  0          ['conv5_block1_2_bn[0][0]']
 n)

 conv5_block1_0_conv (Conv2D)   (None, 10, 10, 2048  2099200     ['conv4_block6_out[0][0]']
                                )

 conv5_block1_3_conv (Conv2D)   (None, 10, 10, 2048  1050624     ['conv5_block1_2_relu[0][0]']
                                )

 conv5_block1_0_bn (BatchNormal  (None, 10, 10, 2048  8192       ['conv5_block1_0_conv[0][0]']
 ization)                       )

 conv5_block1_3_bn (BatchNormal  (None, 10, 10, 2048  8192       ['conv5_block1_3_conv[0][0]']
 ization)                       )

 conv5_block1_add (Add)         (None, 10, 10, 2048  0           ['conv5_block1_0_bn[0][0]',
                                )                                 'conv5_block1_3_bn[0][0]']

 conv5_block1_out (Activation)  (None, 10, 10, 2048  0           ['conv5_block1_add[0][0]']
                                )

 conv5_block2_1_conv (Conv2D)   (None, 10, 10, 512)  1049088     ['conv5_block1_out[0][0]']

 conv5_block2_1_bn (BatchNormal  (None, 10, 10, 512)  2048       ['conv5_block2_1_conv[0][0]']
 ization)

 conv5_block2_1_relu (Activatio  (None, 10, 10, 512)  0          ['conv5_block2_1_bn[0][0]']
 n)

 conv5_block2_2_conv (Conv2D)   (None, 10, 10, 512)  2359808     ['conv5_block2_1_relu[0][0]']

 conv5_block2_2_bn (BatchNormal  (None, 10, 10, 512)  2048       ['conv5_block2_2_conv[0][0]']
 ization)

 conv5_block2_2_relu (Activatio  (None, 10, 10, 512)  0          ['conv5_block2_2_bn[0][0]']
 n)

 conv5_block2_3_conv (Conv2D)   (None, 10, 10, 2048  1050624     ['conv5_block2_2_relu[0][0]']
                                )

 conv5_block2_3_bn (BatchNormal  (None, 10, 10, 2048  8192       ['conv5_block2_3_conv[0][0]']
 ization)                       )

 conv5_block2_add (Add)         (None, 10, 10, 2048  0           ['conv5_block1_out[0][0]',
                                )                                 'conv5_block2_3_bn[0][0]']

 conv5_block2_out (Activation)  (None, 10, 10, 2048  0           ['conv5_block2_add[0][0]']
                                )

 conv5_block3_1_conv (Conv2D)   (None, 10, 10, 512)  1049088     ['conv5_block2_out[0][0]']

 conv5_block3_1_bn (BatchNormal  (None, 10, 10, 512)  2048       ['conv5_block3_1_conv[0][0]']
 ization)

 conv5_block3_1_relu (Activatio  (None, 10, 10, 512)  0          ['conv5_block3_1_bn[0][0]']
 n)

 conv5_block3_2_conv (Conv2D)   (None, 10, 10, 512)  2359808     ['conv5_block3_1_relu[0][0]']

 conv5_block3_2_bn (BatchNormal  (None, 10, 10, 512)  2048       ['conv5_block3_2_conv[0][0]']
 ization)

 conv5_block3_2_relu (Activatio  (None, 10, 10, 512)  0          ['conv5_block3_2_bn[0][0]']
 n)

 conv5_block3_3_conv (Conv2D)   (None, 10, 10, 2048  1050624     ['conv5_block3_2_relu[0][0]']
                                )

 conv5_block3_3_bn (BatchNormal  (None, 10, 10, 2048  8192       ['conv5_block3_3_conv[0][0]']
 ization)                       )

 conv5_block3_add (Add)         (None, 10, 10, 2048  0           ['conv5_block2_out[0][0]',
                                )                                 'conv5_block3_3_bn[0][0]']

 conv5_block3_out (Activation)  (None, 10, 10, 2048  0           ['conv5_block3_add[0][0]']
                                )

==================================================================================================
Total params: 23,587,712
Trainable params: 23,534,592
Non-trainable params: 53,120
__________________________________________________________________________________________________
```
</details>
for feature_map_shape valu can be seen in `conv4_block6_out` which is 19

### MobileNetV2
```python
from tensorflow.keras.applications.mobilenet_v2 import MobileNetV2
a = MobileNetV2(include_top=False, input_shape = [300, 300, 3])
a.summary()
```
Ignore the warning when you initialize MobileNetV2 model.

And so the output will be like this
<details><summary>View Output</summary>
```
__________________________________________________________________________________________________
 Layer (type)                   Output Shape         Param #     Connected to
==================================================================================================
 input_1 (InputLayer)           [(None, 300, 300, 3  0           []
                                )]

 Conv1 (Conv2D)                 (None, 150, 150, 32  864         ['input_1[0][0]']
                                )

 bn_Conv1 (BatchNormalization)  (None, 150, 150, 32  128         ['Conv1[0][0]']
                                )

 Conv1_relu (ReLU)              (None, 150, 150, 32  0           ['bn_Conv1[0][0]']
                                )

 expanded_conv_depthwise (Depth  (None, 150, 150, 32  288        ['Conv1_relu[0][0]']
 wiseConv2D)                    )

 expanded_conv_depthwise_BN (Ba  (None, 150, 150, 32  128        ['expanded_conv_depthwise[0][0]']
 tchNormalization)              )

 expanded_conv_depthwise_relu (  (None, 150, 150, 32  0          ['expanded_conv_depthwise_BN[0][0
 ReLU)                          )                                ]']

 expanded_conv_project (Conv2D)  (None, 150, 150, 16  512        ['expanded_conv_depthwise_relu[0]
                                )                                [0]']

 expanded_conv_project_BN (Batc  (None, 150, 150, 16  64         ['expanded_conv_project[0][0]']
 hNormalization)                )

 block_1_expand (Conv2D)        (None, 150, 150, 96  1536        ['expanded_conv_project_BN[0][0]'
                                )                                ]

 block_1_expand_BN (BatchNormal  (None, 150, 150, 96  384        ['block_1_expand[0][0]']
 ization)                       )

 block_1_expand_relu (ReLU)     (None, 150, 150, 96  0           ['block_1_expand_BN[0][0]']
                                )

 block_1_pad (ZeroPadding2D)    (None, 151, 151, 96  0           ['block_1_expand_relu[0][0]']
                                )

 block_1_depthwise (DepthwiseCo  (None, 75, 75, 96)  864         ['block_1_pad[0][0]']
 nv2D)

 block_1_depthwise_BN (BatchNor  (None, 75, 75, 96)  384         ['block_1_depthwise[0][0]']
 malization)

 block_1_depthwise_relu (ReLU)  (None, 75, 75, 96)   0           ['block_1_depthwise_BN[0][0]']

 block_1_project (Conv2D)       (None, 75, 75, 24)   2304        ['block_1_depthwise_relu[0][0]']

 block_1_project_BN (BatchNorma  (None, 75, 75, 24)  96          ['block_1_project[0][0]']
 lization)

 block_2_expand (Conv2D)        (None, 75, 75, 144)  3456        ['block_1_project_BN[0][0]']

 block_2_expand_BN (BatchNormal  (None, 75, 75, 144)  576        ['block_2_expand[0][0]']
 ization)

 block_2_expand_relu (ReLU)     (None, 75, 75, 144)  0           ['block_2_expand_BN[0][0]']

 block_2_depthwise (DepthwiseCo  (None, 75, 75, 144)  1296       ['block_2_expand_relu[0][0]']
 nv2D)

 block_2_depthwise_BN (BatchNor  (None, 75, 75, 144)  576        ['block_2_depthwise[0][0]']
 malization)

 block_2_depthwise_relu (ReLU)  (None, 75, 75, 144)  0           ['block_2_depthwise_BN[0][0]']

 block_2_project (Conv2D)       (None, 75, 75, 24)   3456        ['block_2_depthwise_relu[0][0]']

 block_2_project_BN (BatchNorma  (None, 75, 75, 24)  96          ['block_2_project[0][0]']
 lization)

 block_2_add (Add)              (None, 75, 75, 24)   0           ['block_1_project_BN[0][0]',
                                                                  'block_2_project_BN[0][0]']

 block_3_expand (Conv2D)        (None, 75, 75, 144)  3456        ['block_2_add[0][0]']

 block_3_expand_BN (BatchNormal  (None, 75, 75, 144)  576        ['block_3_expand[0][0]']
 ization)

 block_3_expand_relu (ReLU)     (None, 75, 75, 144)  0           ['block_3_expand_BN[0][0]']

 block_3_pad (ZeroPadding2D)    (None, 77, 77, 144)  0           ['block_3_expand_relu[0][0]']

 block_3_depthwise (DepthwiseCo  (None, 38, 38, 144)  1296       ['block_3_pad[0][0]']
 nv2D)

 block_3_depthwise_BN (BatchNor  (None, 38, 38, 144)  576        ['block_3_depthwise[0][0]']
 malization)

 block_3_depthwise_relu (ReLU)  (None, 38, 38, 144)  0           ['block_3_depthwise_BN[0][0]']

 block_3_project (Conv2D)       (None, 38, 38, 32)   4608        ['block_3_depthwise_relu[0][0]']

 block_3_project_BN (BatchNorma  (None, 38, 38, 32)  128         ['block_3_project[0][0]']
 lization)

 block_4_expand (Conv2D)        (None, 38, 38, 192)  6144        ['block_3_project_BN[0][0]']

 block_4_expand_BN (BatchNormal  (None, 38, 38, 192)  768        ['block_4_expand[0][0]']
 ization)

 block_4_expand_relu (ReLU)     (None, 38, 38, 192)  0           ['block_4_expand_BN[0][0]']

 block_4_depthwise (DepthwiseCo  (None, 38, 38, 192)  1728       ['block_4_expand_relu[0][0]']
 nv2D)

 block_4_depthwise_BN (BatchNor  (None, 38, 38, 192)  768        ['block_4_depthwise[0][0]']
 malization)

 block_4_depthwise_relu (ReLU)  (None, 38, 38, 192)  0           ['block_4_depthwise_BN[0][0]']

 block_4_project (Conv2D)       (None, 38, 38, 32)   6144        ['block_4_depthwise_relu[0][0]']

 block_4_project_BN (BatchNorma  (None, 38, 38, 32)  128         ['block_4_project[0][0]']
 lization)

 block_4_add (Add)              (None, 38, 38, 32)   0           ['block_3_project_BN[0][0]',
                                                                  'block_4_project_BN[0][0]']

 block_5_expand (Conv2D)        (None, 38, 38, 192)  6144        ['block_4_add[0][0]']

 block_5_expand_BN (BatchNormal  (None, 38, 38, 192)  768        ['block_5_expand[0][0]']
 ization)

 block_5_expand_relu (ReLU)     (None, 38, 38, 192)  0           ['block_5_expand_BN[0][0]']

 block_5_depthwise (DepthwiseCo  (None, 38, 38, 192)  1728       ['block_5_expand_relu[0][0]']
 nv2D)

 block_5_depthwise_BN (BatchNor  (None, 38, 38, 192)  768        ['block_5_depthwise[0][0]']
 malization)

 block_5_depthwise_relu (ReLU)  (None, 38, 38, 192)  0           ['block_5_depthwise_BN[0][0]']

 block_5_project (Conv2D)       (None, 38, 38, 32)   6144        ['block_5_depthwise_relu[0][0]']

 block_5_project_BN (BatchNorma  (None, 38, 38, 32)  128         ['block_5_project[0][0]']
 lization)

 block_5_add (Add)              (None, 38, 38, 32)   0           ['block_4_add[0][0]',
                                                                  'block_5_project_BN[0][0]']

 block_6_expand (Conv2D)        (None, 38, 38, 192)  6144        ['block_5_add[0][0]']

 block_6_expand_BN (BatchNormal  (None, 38, 38, 192)  768        ['block_6_expand[0][0]']
 ization)

 block_6_expand_relu (ReLU)     (None, 38, 38, 192)  0           ['block_6_expand_BN[0][0]']

 block_6_pad (ZeroPadding2D)    (None, 39, 39, 192)  0           ['block_6_expand_relu[0][0]']

 block_6_depthwise (DepthwiseCo  (None, 19, 19, 192)  1728       ['block_6_pad[0][0]']
 nv2D)

 block_6_depthwise_BN (BatchNor  (None, 19, 19, 192)  768        ['block_6_depthwise[0][0]']
 malization)

 block_6_depthwise_relu (ReLU)  (None, 19, 19, 192)  0           ['block_6_depthwise_BN[0][0]']

 block_6_project (Conv2D)       (None, 19, 19, 64)   12288       ['block_6_depthwise_relu[0][0]']

 block_6_project_BN (BatchNorma  (None, 19, 19, 64)  256         ['block_6_project[0][0]']
 lization)

 block_7_expand (Conv2D)        (None, 19, 19, 384)  24576       ['block_6_project_BN[0][0]']

 block_7_expand_BN (BatchNormal  (None, 19, 19, 384)  1536       ['block_7_expand[0][0]']
 ization)

 block_7_expand_relu (ReLU)     (None, 19, 19, 384)  0           ['block_7_expand_BN[0][0]']

 block_7_depthwise (DepthwiseCo  (None, 19, 19, 384)  3456       ['block_7_expand_relu[0][0]']
 nv2D)

 block_7_depthwise_BN (BatchNor  (None, 19, 19, 384)  1536       ['block_7_depthwise[0][0]']
 malization)

 block_7_depthwise_relu (ReLU)  (None, 19, 19, 384)  0           ['block_7_depthwise_BN[0][0]']

 block_7_project (Conv2D)       (None, 19, 19, 64)   24576       ['block_7_depthwise_relu[0][0]']

 block_7_project_BN (BatchNorma  (None, 19, 19, 64)  256         ['block_7_project[0][0]']
 lization)

 block_7_add (Add)              (None, 19, 19, 64)   0           ['block_6_project_BN[0][0]',
                                                                  'block_7_project_BN[0][0]']

 block_8_expand (Conv2D)        (None, 19, 19, 384)  24576       ['block_7_add[0][0]']

 block_8_expand_BN (BatchNormal  (None, 19, 19, 384)  1536       ['block_8_expand[0][0]']
 ization)

 block_8_expand_relu (ReLU)     (None, 19, 19, 384)  0           ['block_8_expand_BN[0][0]']

 block_8_depthwise (DepthwiseCo  (None, 19, 19, 384)  3456       ['block_8_expand_relu[0][0]']
 nv2D)

 block_8_depthwise_BN (BatchNor  (None, 19, 19, 384)  1536       ['block_8_depthwise[0][0]']
 malization)

 block_8_depthwise_relu (ReLU)  (None, 19, 19, 384)  0           ['block_8_depthwise_BN[0][0]']

 block_8_project (Conv2D)       (None, 19, 19, 64)   24576       ['block_8_depthwise_relu[0][0]']

 block_8_project_BN (BatchNorma  (None, 19, 19, 64)  256         ['block_8_project[0][0]']
 lization)

 block_8_add (Add)              (None, 19, 19, 64)   0           ['block_7_add[0][0]',
                                                                  'block_8_project_BN[0][0]']

 block_9_expand (Conv2D)        (None, 19, 19, 384)  24576       ['block_8_add[0][0]']

 block_9_expand_BN (BatchNormal  (None, 19, 19, 384)  1536       ['block_9_expand[0][0]']
 ization)

 block_9_expand_relu (ReLU)     (None, 19, 19, 384)  0           ['block_9_expand_BN[0][0]']

 block_9_depthwise (DepthwiseCo  (None, 19, 19, 384)  3456       ['block_9_expand_relu[0][0]']
 nv2D)

 block_9_depthwise_BN (BatchNor  (None, 19, 19, 384)  1536       ['block_9_depthwise[0][0]']
 malization)

 block_9_depthwise_relu (ReLU)  (None, 19, 19, 384)  0           ['block_9_depthwise_BN[0][0]']

 block_9_project (Conv2D)       (None, 19, 19, 64)   24576       ['block_9_depthwise_relu[0][0]']

 block_9_project_BN (BatchNorma  (None, 19, 19, 64)  256         ['block_9_project[0][0]']
 lization)

 block_9_add (Add)              (None, 19, 19, 64)   0           ['block_8_add[0][0]',
                                                                  'block_9_project_BN[0][0]']

 block_10_expand (Conv2D)       (None, 19, 19, 384)  24576       ['block_9_add[0][0]']

 block_10_expand_BN (BatchNorma  (None, 19, 19, 384)  1536       ['block_10_expand[0][0]']
 lization)

 block_10_expand_relu (ReLU)    (None, 19, 19, 384)  0           ['block_10_expand_BN[0][0]']

 block_10_depthwise (DepthwiseC  (None, 19, 19, 384)  3456       ['block_10_expand_relu[0][0]']
 onv2D)

 block_10_depthwise_BN (BatchNo  (None, 19, 19, 384)  1536       ['block_10_depthwise[0][0]']
 rmalization)

 block_10_depthwise_relu (ReLU)  (None, 19, 19, 384)  0          ['block_10_depthwise_BN[0][0]']

 block_10_project (Conv2D)      (None, 19, 19, 96)   36864       ['block_10_depthwise_relu[0][0]']

 block_10_project_BN (BatchNorm  (None, 19, 19, 96)  384         ['block_10_project[0][0]']
 alization)

 block_11_expand (Conv2D)       (None, 19, 19, 576)  55296       ['block_10_project_BN[0][0]']

 block_11_expand_BN (BatchNorma  (None, 19, 19, 576)  2304       ['block_11_expand[0][0]']
 lization)

 block_11_expand_relu (ReLU)    (None, 19, 19, 576)  0           ['block_11_expand_BN[0][0]']

 block_11_depthwise (DepthwiseC  (None, 19, 19, 576)  5184       ['block_11_expand_relu[0][0]']
 onv2D)

 block_11_depthwise_BN (BatchNo  (None, 19, 19, 576)  2304       ['block_11_depthwise[0][0]']
 rmalization)

 block_11_depthwise_relu (ReLU)  (None, 19, 19, 576)  0          ['block_11_depthwise_BN[0][0]']

 block_11_project (Conv2D)      (None, 19, 19, 96)   55296       ['block_11_depthwise_relu[0][0]']

 block_11_project_BN (BatchNorm  (None, 19, 19, 96)  384         ['block_11_project[0][0]']
 alization)

 block_11_add (Add)             (None, 19, 19, 96)   0           ['block_10_project_BN[0][0]',
                                                                  'block_11_project_BN[0][0]']

 block_12_expand (Conv2D)       (None, 19, 19, 576)  55296       ['block_11_add[0][0]']

 block_12_expand_BN (BatchNorma  (None, 19, 19, 576)  2304       ['block_12_expand[0][0]']
 lization)

 block_12_expand_relu (ReLU)    (None, 19, 19, 576)  0           ['block_12_expand_BN[0][0]']

 block_12_depthwise (DepthwiseC  (None, 19, 19, 576)  5184       ['block_12_expand_relu[0][0]']
 onv2D)

 block_12_depthwise_BN (BatchNo  (None, 19, 19, 576)  2304       ['block_12_depthwise[0][0]']
 rmalization)

 block_12_depthwise_relu (ReLU)  (None, 19, 19, 576)  0          ['block_12_depthwise_BN[0][0]']

 block_12_project (Conv2D)      (None, 19, 19, 96)   55296       ['block_12_depthwise_relu[0][0]']

 block_12_project_BN (BatchNorm  (None, 19, 19, 96)  384         ['block_12_project[0][0]']
 alization)

 block_12_add (Add)             (None, 19, 19, 96)   0           ['block_11_add[0][0]',
                                                                  'block_12_project_BN[0][0]']

 block_13_expand (Conv2D)       (None, 19, 19, 576)  55296       ['block_12_add[0][0]']

 block_13_expand_BN (BatchNorma  (None, 19, 19, 576)  2304       ['block_13_expand[0][0]']
 lization)

 block_13_expand_relu (ReLU)    (None, 19, 19, 576)  0           ['block_13_expand_BN[0][0]']

 block_13_pad (ZeroPadding2D)   (None, 21, 21, 576)  0           ['block_13_expand_relu[0][0]']

 block_13_depthwise (DepthwiseC  (None, 10, 10, 576)  5184       ['block_13_pad[0][0]']
 onv2D)

 block_13_depthwise_BN (BatchNo  (None, 10, 10, 576)  2304       ['block_13_depthwise[0][0]']
 rmalization)

 block_13_depthwise_relu (ReLU)  (None, 10, 10, 576)  0          ['block_13_depthwise_BN[0][0]']

 block_13_project (Conv2D)      (None, 10, 10, 160)  92160       ['block_13_depthwise_relu[0][0]']

 block_13_project_BN (BatchNorm  (None, 10, 10, 160)  640        ['block_13_project[0][0]']
 alization)

 block_14_expand (Conv2D)       (None, 10, 10, 960)  153600      ['block_13_project_BN[0][0]']

 block_14_expand_BN (BatchNorma  (None, 10, 10, 960)  3840       ['block_14_expand[0][0]']
 lization)

 block_14_expand_relu (ReLU)    (None, 10, 10, 960)  0           ['block_14_expand_BN[0][0]']

 block_14_depthwise (DepthwiseC  (None, 10, 10, 960)  8640       ['block_14_expand_relu[0][0]']
 onv2D)

 block_14_depthwise_BN (BatchNo  (None, 10, 10, 960)  3840       ['block_14_depthwise[0][0]']
 rmalization)

 block_14_depthwise_relu (ReLU)  (None, 10, 10, 960)  0          ['block_14_depthwise_BN[0][0]']

 block_14_project (Conv2D)      (None, 10, 10, 160)  153600      ['block_14_depthwise_relu[0][0]']

 block_14_project_BN (BatchNorm  (None, 10, 10, 160)  640        ['block_14_project[0][0]']
 alization)

 block_14_add (Add)             (None, 10, 10, 160)  0           ['block_13_project_BN[0][0]',
                                                                  'block_14_project_BN[0][0]']

 block_15_expand (Conv2D)       (None, 10, 10, 960)  153600      ['block_14_add[0][0]']

 block_15_expand_BN (BatchNorma  (None, 10, 10, 960)  3840       ['block_15_expand[0][0]']
 lization)

 block_15_expand_relu (ReLU)    (None, 10, 10, 960)  0           ['block_15_expand_BN[0][0]']

 block_15_depthwise (DepthwiseC  (None, 10, 10, 960)  8640       ['block_15_expand_relu[0][0]']
 onv2D)

 block_15_depthwise_BN (BatchNo  (None, 10, 10, 960)  3840       ['block_15_depthwise[0][0]']
 rmalization)

 block_15_depthwise_relu (ReLU)  (None, 10, 10, 960)  0          ['block_15_depthwise_BN[0][0]']

 block_15_project (Conv2D)      (None, 10, 10, 160)  153600      ['block_15_depthwise_relu[0][0]']

 block_15_project_BN (BatchNorm  (None, 10, 10, 160)  640        ['block_15_project[0][0]']
 alization)

 block_15_add (Add)             (None, 10, 10, 160)  0           ['block_14_add[0][0]',
                                                                  'block_15_project_BN[0][0]']

 block_16_expand (Conv2D)       (None, 10, 10, 960)  153600      ['block_15_add[0][0]']

 block_16_expand_BN (BatchNorma  (None, 10, 10, 960)  3840       ['block_16_expand[0][0]']
 lization)

 block_16_expand_relu (ReLU)    (None, 10, 10, 960)  0           ['block_16_expand_BN[0][0]']

 block_16_depthwise (DepthwiseC  (None, 10, 10, 960)  8640       ['block_16_expand_relu[0][0]']
 onv2D)

 block_16_depthwise_BN (BatchNo  (None, 10, 10, 960)  3840       ['block_16_depthwise[0][0]']
 rmalization)

 block_16_depthwise_relu (ReLU)  (None, 10, 10, 960)  0          ['block_16_depthwise_BN[0][0]']

 block_16_project (Conv2D)      (None, 10, 10, 320)  307200      ['block_16_depthwise_relu[0][0]']

 block_16_project_BN (BatchNorm  (None, 10, 10, 320)  1280       ['block_16_project[0][0]']
 alization)

 Conv_1 (Conv2D)                (None, 10, 10, 1280  409600      ['block_16_project_BN[0][0]']
                                )

 Conv_1_bn (BatchNormalization)  (None, 10, 10, 1280  5120       ['Conv_1[0][0]']
                                )

 out_relu (ReLU)                (None, 10, 10, 1280  0           ['Conv_1_bn[0][0]']
                                )

==================================================================================================
Total params: 2,257,984
Trainable params: 2,223,872
Non-trainable params: 34,112
__________________________________________________________________________________________________
```
</details>
for feature_map_shape valu can be seen in `block_13_expand_relu` which is 19

> This Python project framework forked from [FurkanOM TF Faster R-CNN Repository](https://github.com/FurkanOM/tf-faster-rcnn)
> You can see my dataset [here](https://drive.google.com/drive/folders/1OocfroKRiXdaOxXxe71HCeD9TI_huDO6?usp=sharing)

## Android (Mobile)
The Android App called RoDa Detection (Road Damage Detection)

Few notes for this section
1. The camera function is use native camera from device (not from jetpack), some device may occurring an force close error
2. In this project the model wasn't saved in mobile, but it is stored in a server. So to fetch the picture is using Retrofit

3. Put your BASE_URL in your local.properties file, example
   ```
   BASE_URL="your link here"
   ```
4. This Mobile use MVVM architecture

## Server
[Uploaded in different repository.](https://github.com/ruman1609/pothole_faster_rcnn_server)