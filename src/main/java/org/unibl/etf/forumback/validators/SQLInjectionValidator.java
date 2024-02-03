package org.unibl.etf.forumback.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLInjectionValidator implements ConstraintValidator<SQLInjectionMatch, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        List<Pattern> patterns = Arrays.asList(

                Pattern.compile("(?i)(.*)(\\b)+(OR|AND)(\\s)+(true|false)(\\s)*(.*)", Pattern.CASE_INSENSITIVE),
                // SQL Injection sa uslovom jednakosti
                Pattern.compile("(?i)(.*)(\\b)+(OR|AND)(\\s)+(\\w)(\\s)*(\\=)(\\s)*(\\w)(\\s)*(.*)", Pattern.CASE_INSENSITIVE),
                // Selektovanje svih podataka iz tabele
                Pattern.compile("(?i)(.*)(\\b)+SELECT(\\b)+\\s.*(\\b)(.*)", Pattern.CASE_INSENSITIVE),
                // SQL Injection pokušaj brisanja podataka
                Pattern.compile("(?i)(.*)(\\b)+DELETE(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)", Pattern.CASE_INSENSITIVE),
                // SQL Injection pokušaj kreiranja ili menjanja
                Pattern.compile("(?i)(.*)(\\b)+(INSERT|UPDATE|UPSERT|SAVEPOINT|CALL)(\\b)+\\s.*(.*)", Pattern.CASE_INSENSITIVE),
                // SQL Injection pokušaj brisanja tabele
                Pattern.compile("(?i)(.*)(\\b)+DROP(\\b)+\\s.*(.*)", Pattern.CASE_INSENSITIVE),
                // SQL Injection pokušaj sa UNION operacijom
                Pattern.compile("(?i)(.*)(\\b)+UNION(\\b)+\\s+SELECT(\\b)+\\s.*(\\b)(.*)", Pattern.CASE_INSENSITIVE)

        );
        for(var p : patterns){
            Matcher matcher = p.matcher(value);
            if(matcher.find()){
                return false;
            }
        }
        return true;
    }
}
