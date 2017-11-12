# Image Halftone

A Java utility or library for creating halftone-effect images based on a source
image.

## Building

After cloning the project, execute `ant` within the project root directory to
build. The runnable `jar` file will be placed within the `dist` directory.


## Usage

### Command Line

*Usage* 

`image-halftone.jar [options] <input-file> <output-file>`

*Options and defaults*

- `--dot-size=5` - size of dots in pixels
- `--dot-space=2` - space between dots in pixels
- `--dot-shape=dot` - shape of dots, either `dot` or `block`
- `--render-scale=6` - internally scales image up by this factor to ensure smooth dot rendering
- `--bg-color=0,0,0` - background color in format R,G,B, or empty to use the source image as a background
- `--fg-color=255,255,255` - dot color in format R,G,B, or blank to use colour of pixel from source image

### As a Library

You may use this code in your own projects. Refer to the `net.shrimpworks.halftone.Halftone`
class for Javadocs, and the `Main` class for an example of loading an image,
generating output, and saving the generated image to disk.


## Samples

[Sample Gallery](https://imgur.com/a/TIGx4)
