/*
 * AudioTools.java (Class: com.madphysicist.tools.util.AudioTools)
 *
 * Mad Physicist JTools Project (General Purpose Utilities)
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 by Joseph Fox-Rabinovitz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.madphysicist.tools.util;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * A utility class for performing basic tasks with audio files. This class can
 * not be instantiated.
 *
 * @author Joseph Fox-Rabinovitz
 * @version 1.0.0.0, 13 Feb 2013
 * @since 1.0.0.0
 */
public class AudioTools
{
    /**
     * A private constructor to prevent instantiation.
     *
     * @since 1.0.0.0
     */
    private AudioTools() {}

    /**
     * Blocks while playing an audio clip from a file.
     *
     * @param file the file to play.
     * @throws IOException if the file could not be opened or read.
     * @throws InterruptedException if the method could not block while the
     * file was playing.
     * @throws LineUnavailableException if the clip is unavailable due to
     * resource restrictions.
     * @throws UnsupportedAudioFileException if the file format is not
     * supported by the audio system.
     * @since 1.0.0.0
     */
    public static void playClip(File file) throws IOException,
                                                  InterruptedException,
                                                  LineUnavailableException,
                                                  UnsupportedAudioFileException
    {
        final LineListener lineListener = new LineListener() {
            @Override public void update(LineEvent event) {
                if(event.getType() == LineEvent.Type.STOP) {
                    synchronized(this) {
                        this.notifyAll();
                    }
                }
            }
        };

        AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
        Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, inputStream.getFormat()));
        clip.addLineListener(lineListener);
        clip.open(inputStream);
        clip.start();
        synchronized(lineListener) {
            // wait for the clip to finish playing
            lineListener.wait();
        }
    }
}
