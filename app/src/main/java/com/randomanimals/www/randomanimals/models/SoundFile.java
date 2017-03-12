package com.randomanimals.www.randomanimals.models;

/**
 * Created by cbuonocore on 3/11/17.
 */

public class SoundFile {

    String fileName;
    String animal;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public SoundFile(String fileName, String animal) {

        this.fileName = fileName;
        this.animal = animal;
    }

    @Override
    public String toString() {
        return "SoundFile{" +
                "fileName='" + fileName + '\'' +
                ", animal='" + animal + '\'' +
                '}';
    }
}
