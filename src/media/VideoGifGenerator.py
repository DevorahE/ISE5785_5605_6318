import os
from moviepy import ImageSequenceClip

# Directory containing the original images
source_dir = r"C:\Users\דבורה\IdeaProjects\ISE5785_5605_6318\images"

# Build the list of image file paths
image_files = []

# Add images from 43 down to 0
for i in range(43, -1, -1):
    filename = f"Diamond_Video_Frame_{i:04d}.png"
    filepath = os.path.join(source_dir, filename)
    if os.path.exists(filepath):
        image_files.append(filepath)
    else:
        print(f"Missing file: {filepath}")

# Add images from 119 down to 77
for i in range(119, 76, -1):
    filename = f"Diamond_Video_Frame_{i:04d}.png"
    filepath = os.path.join(source_dir, filename)
    if os.path.exists(filepath):
        image_files.append(filepath)
    else:
        print(f"Missing file: {filepath}")

# Add images from 77 up to 119
for i in range(77, 120, 1):
    filename = f"Diamond_Video_Frame_{i:04d}.png"
    filepath = os.path.join(source_dir, filename)
    if os.path.exists(filepath):
        image_files.append(filepath)
    else:
        print(f"Missing file: {filepath}")

# Add images from 0 up to 43
for i in range(0, 44, 1):
    filename = f"Diamond_Video_Frame_{i:04d}.png"
    filepath = os.path.join(source_dir, filename)
    if os.path.exists(filepath):
        image_files.append(filepath)
    else:
        print(f"Missing file: {filepath}")

# Check if any images were found
if not image_files:
    print("No images found.")
else:
    # Create the gif (20 frames per second)
    clip = ImageSequenceClip(image_files, fps=20)
    clip.write_videofile("diamond.mp4", codec="libx264", fps=20)
    clip.write_gif("diamond.gif", fps=20)
