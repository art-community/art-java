package ru.art.generator.mapper.models;

import lombok.Builder;
import lombok.Setter;
import lombok.Value;

@Builder
@Value
@Setter
public class GenerationPackageModel {
    private String startPackagePathCompiled;    //full path to compiled parent package for model package
    private String startPackagePath;            //full path to non compiled parent package for model package
    private String startPackage;                //full parent package for model package
    private String modelPackagePathCompiled;    //full path to compiled model package
    private String modelPackagePath;            //full path to non compiled model package
    private String modelPackage;                //full package with models
    private String genPackagePathCompiled;      //full path to compiled generation package
    private String genPackagePath;              //full path to non compiled generation package
    private String genPackage;                  //full package for generation
    private String jarPathToMain;               //compiled path taken from jar till main
}

