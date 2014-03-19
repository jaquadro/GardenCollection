using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Imaging;

namespace OverlayTransform
{
    public partial class Form1 : Form
    {
        Bitmap sourceImage;
        Bitmap clayImage = new Bitmap(64, 64, System.Drawing.Imaging.PixelFormat.Format32bppArgb);

        public Form1 ()
        {
            InitializeComponent();

            using (Graphics g = Graphics.FromImage(clayImage)) {
                g.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.NearestNeighbor;
                g.PixelOffsetMode = System.Drawing.Drawing2D.PixelOffsetMode.Half;
                g.DrawImage(_previewOverlay.Image, 0, 0, 64, 64);
            }

            _previewOverlay.Image = clayImage;
        }

        private void _buttonOpen_Click (object sender, EventArgs e)
        {
            OpenFileDialog openFileDialog1 = new OpenFileDialog() {
                Filter = "Image files (*.png)|*.png",
                RestoreDirectory = true,
            };

            if (openFileDialog1.ShowDialog() == DialogResult.OK) {
                using (Stream fileStream = openFileDialog1.OpenFile()) {
                    sourceImage = (Bitmap)Bitmap.FromStream(fileStream);

                    Bitmap dest = new Bitmap(64, 64, System.Drawing.Imaging.PixelFormat.Format32bppArgb);
                    using (Graphics g = Graphics.FromImage(dest)) {
                        g.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.NearestNeighbor;
                        g.PixelOffsetMode = System.Drawing.Drawing2D.PixelOffsetMode.Half;
                        g.DrawImage(sourceImage, 0, 0, 64, 64);
                    }
                    _previewOriginal.Image = dest;

                    UpdateOverlay(dest);
                }
            }
        }

        private Bitmap CurrentOverlay (Bitmap source)
        {
            if (radioButton1.Checked)
                return MultiplyOverlay(source);
            else if (radioButton2.Checked)
                return MultiplyScreenOverlay(source);
            else if (radioButton3.Checked)
                return ScreenOverlay(source);

            return null;
        }

        private void UpdateOverlay (Bitmap source)
        {
            _previewTransform.Image = CurrentOverlay(source);
            _previewOverlay.Image = ApplyOverlay((Bitmap)_previewTransform.Image, clayImage);
        }

        private Bitmap MultiplyOverlay (Bitmap source)
        {
            Bitmap dest = new Bitmap(source.Width, source.Height, PixelFormat.Format32bppArgb);
            for (int y = 0; y < source.Height; y++) {
                for (int x = 0; x < source.Width; x++) {
                    Color c = source.GetPixel(x, y);
                    dest.SetPixel(x, y, Color.FromArgb((255 - c.R) / 2, 0, 0, 0));
                }
            }

            return dest;
        }

        private Bitmap ScreenOverlay (Bitmap source)
        {
            Bitmap dest = new Bitmap(source.Width, source.Height, PixelFormat.Format32bppArgb);
            for (int y = 0; y < source.Height; y++) {
                for (int x = 0; x < source.Width; x++) {
                    Color c = source.GetPixel(x, y);
                    dest.SetPixel(x, y, Color.FromArgb(c.R / 2, 255, 255, 255));
                }
            }

            return dest;
        }

        private Bitmap MultiplyScreenOverlay (Bitmap source)
        {
            Bitmap dest = new Bitmap(source.Width, source.Height, PixelFormat.Format32bppArgb);
            for (int y = 0; y < source.Height; y++) {
                for (int x = 0; x < source.Width; x++) {
                    Color c = source.GetPixel(x, y);
                    if (c.R < 128)
                        dest.SetPixel(x, y, Color.FromArgb(128 - c.R, 0, 0, 0));
                    else
                        dest.SetPixel(x, y, Color.FromArgb(c.R - 128, 255, 255, 255));
                }
            }

            return dest;
        }

        private Bitmap ApplyOverlay (Bitmap source, Bitmap target)
        {
            Bitmap dest = new Bitmap(source.Width, source.Height, source.PixelFormat);
            for (int y = 0; y < source.Height; y++) {
                for (int x = 0; x < source.Width; x++) {
                    Color cs = source.GetPixel(x, y);
                    Color ct = target.GetPixel(x, y);

                    int r = (ct.R * (255 - cs.A) + cs.R * cs.A) / 255;
                    int g = (ct.G * (255 - cs.A) + cs.G * cs.A) / 255;
                    int b = (ct.B * (255 - cs.A) + cs.B * cs.A) / 255;
                    dest.SetPixel(x, y, Color.FromArgb(255, r, g, b));
                }
            }

            return dest;
        }

        private void _buttonSave_Click (object sender, EventArgs e)
        {
            SaveFileDialog openFileDialog1 = new SaveFileDialog() {
                Filter = "Image files (*.png)|*.png",
                RestoreDirectory = true,
            };

            if (openFileDialog1.ShowDialog() == DialogResult.OK) {
                using (Stream fileStream = openFileDialog1.OpenFile()) {
                    CurrentOverlay(sourceImage).Save(fileStream, ImageFormat.Png);
                }
            }
        }

        private void radioButton1_CheckedChanged (object sender, EventArgs e)
        {
            UpdateOverlay((Bitmap)_previewOriginal.Image);
        }

        private void radioButton2_CheckedChanged (object sender, EventArgs e)
        {
            UpdateOverlay((Bitmap)_previewOriginal.Image);
        }

        private void radioButton3_CheckedChanged (object sender, EventArgs e)
        {
            UpdateOverlay((Bitmap)_previewOriginal.Image);
        }
    }
}
