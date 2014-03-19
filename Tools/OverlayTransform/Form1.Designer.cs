namespace OverlayTransform
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose (bool disposing)
        {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent ()
        {
            this._previewOriginal = new System.Windows.Forms.PictureBox();
            this._buttonOpen = new System.Windows.Forms.Button();
            this._previewTransform = new System.Windows.Forms.PictureBox();
            this._previewOverlay = new System.Windows.Forms.PictureBox();
            this._buttonSave = new System.Windows.Forms.Button();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.radioButton1 = new System.Windows.Forms.RadioButton();
            this.radioButton2 = new System.Windows.Forms.RadioButton();
            this.radioButton3 = new System.Windows.Forms.RadioButton();
            ((System.ComponentModel.ISupportInitialize)(this._previewOriginal)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this._previewTransform)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this._previewOverlay)).BeginInit();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // _previewOriginal
            // 
            this._previewOriginal.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this._previewOriginal.Location = new System.Drawing.Point(12, 12);
            this._previewOriginal.Name = "_previewOriginal";
            this._previewOriginal.Size = new System.Drawing.Size(66, 66);
            this._previewOriginal.TabIndex = 0;
            this._previewOriginal.TabStop = false;
            // 
            // _buttonOpen
            // 
            this._buttonOpen.Location = new System.Drawing.Point(12, 82);
            this._buttonOpen.Name = "_buttonOpen";
            this._buttonOpen.Size = new System.Drawing.Size(66, 23);
            this._buttonOpen.TabIndex = 1;
            this._buttonOpen.Text = "Open";
            this._buttonOpen.UseVisualStyleBackColor = true;
            this._buttonOpen.Click += new System.EventHandler(this._buttonOpen_Click);
            // 
            // _previewTransform
            // 
            this._previewTransform.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this._previewTransform.Location = new System.Drawing.Point(84, 12);
            this._previewTransform.Name = "_previewTransform";
            this._previewTransform.Size = new System.Drawing.Size(66, 66);
            this._previewTransform.TabIndex = 2;
            this._previewTransform.TabStop = false;
            // 
            // _previewOverlay
            // 
            this._previewOverlay.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this._previewOverlay.Image = global::OverlayTransform.Properties.Resources.large_pot;
            this._previewOverlay.Location = new System.Drawing.Point(156, 12);
            this._previewOverlay.Name = "_previewOverlay";
            this._previewOverlay.Size = new System.Drawing.Size(66, 66);
            this._previewOverlay.TabIndex = 3;
            this._previewOverlay.TabStop = false;
            // 
            // _buttonSave
            // 
            this._buttonSave.Location = new System.Drawing.Point(84, 82);
            this._buttonSave.Name = "_buttonSave";
            this._buttonSave.Size = new System.Drawing.Size(66, 23);
            this._buttonSave.TabIndex = 4;
            this._buttonSave.Text = "Save";
            this._buttonSave.UseVisualStyleBackColor = true;
            this._buttonSave.Click += new System.EventHandler(this._buttonSave_Click);
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.radioButton3);
            this.groupBox1.Controls.Add(this.radioButton2);
            this.groupBox1.Controls.Add(this.radioButton1);
            this.groupBox1.Location = new System.Drawing.Point(12, 111);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(210, 93);
            this.groupBox1.TabIndex = 5;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Overlay Function";
            // 
            // radioButton1
            // 
            this.radioButton1.AutoSize = true;
            this.radioButton1.Checked = true;
            this.radioButton1.Location = new System.Drawing.Point(6, 19);
            this.radioButton1.Name = "radioButton1";
            this.radioButton1.Size = new System.Drawing.Size(75, 17);
            this.radioButton1.TabIndex = 0;
            this.radioButton1.TabStop = true;
            this.radioButton1.Text = "Multiply 50";
            this.radioButton1.UseVisualStyleBackColor = true;
            this.radioButton1.CheckedChanged += new System.EventHandler(this.radioButton1_CheckedChanged);
            // 
            // radioButton2
            // 
            this.radioButton2.AutoSize = true;
            this.radioButton2.Location = new System.Drawing.Point(6, 42);
            this.radioButton2.Name = "radioButton2";
            this.radioButton2.Size = new System.Drawing.Size(105, 17);
            this.radioButton2.TabIndex = 1;
            this.radioButton2.Text = "Multiply / Screen";
            this.radioButton2.UseVisualStyleBackColor = true;
            this.radioButton2.CheckedChanged += new System.EventHandler(this.radioButton2_CheckedChanged);
            // 
            // radioButton3
            // 
            this.radioButton3.AutoSize = true;
            this.radioButton3.Location = new System.Drawing.Point(6, 65);
            this.radioButton3.Name = "radioButton3";
            this.radioButton3.Size = new System.Drawing.Size(74, 17);
            this.radioButton3.TabIndex = 2;
            this.radioButton3.Text = "Screen 50";
            this.radioButton3.UseVisualStyleBackColor = true;
            this.radioButton3.CheckedChanged += new System.EventHandler(this.radioButton3_CheckedChanged);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(234, 216);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this._buttonSave);
            this.Controls.Add(this._previewOverlay);
            this.Controls.Add(this._previewTransform);
            this.Controls.Add(this._buttonOpen);
            this.Controls.Add(this._previewOriginal);
            this.Name = "Form1";
            this.Text = "Form1";
            ((System.ComponentModel.ISupportInitialize)(this._previewOriginal)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this._previewTransform)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this._previewOverlay)).EndInit();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.PictureBox _previewOriginal;
        private System.Windows.Forms.Button _buttonOpen;
        private System.Windows.Forms.PictureBox _previewTransform;
        private System.Windows.Forms.PictureBox _previewOverlay;
        private System.Windows.Forms.Button _buttonSave;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.RadioButton radioButton3;
        private System.Windows.Forms.RadioButton radioButton2;
        private System.Windows.Forms.RadioButton radioButton1;
    }
}

