package countries;

import java.io.IOException;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.*;

import java.time.ZoneId;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Homework2 {

    long charCount(String s, char c){
        return s.chars().filter(character->Character.toLowerCase(character)==c).count();
    }

    long vowelCount(String s){
        return s.chars().filter(character->Character.toString(Character.toLowerCase(character)).matches("[aeiou]")).count();
    }

    private List<Country> countries;

    public Homework2() {
        countries = new CountryRepository().getAll();
    }

    /**
     * Returns the longest country name translation.
     */
    public Optional<String> streamPipeline1() {
        return countries.stream().flatMap(country->country.getTranslations().entrySet().stream().map(Map.Entry::getValue)).max(Comparator.comparing(String::length));
    }

    /**
     * Returns the longest Italian (i.e., {@code "it"}) country name translation.
     */
    public Optional<String> streamPipeline2() {
        return countries.stream().flatMap(country->country.getTranslations().entrySet().stream()).filter(translation->translation.getKey()=="it").map(Map.Entry::getValue).max(Comparator.comparing(String::length));
    }

    /**
     * Prints the longest country name translation together with its language code in the form language=translation.
     */
    public void streamPipeline3() {
        System.out.println(countries.stream().flatMap(country->country.getTranslations().entrySet().stream()).max(Comparator.comparing(m->m.getValue().length())).orElse(null));
    }

    /**
     * Prints single word country names (i.e., country names that do not contain any space characters).
     */
    public void streamPipeline4() {
        countries.stream().filter(country -> !country.getName().contains(" ")).map(Country::getName).forEach(System.out::println);
    }

    /**
     * Returns the country name with the most number of words.
     */
    public Optional<String> streamPipeline5() {
        return countries.stream().map(Country::getName).max(Comparator.comparing(name->name.split(" ").length));
    }

    /**
     * Returns whether there exists at least one capital that is a palindrome.
     */
    public boolean streamPipeline6() {
        return countries.stream().anyMatch(country -> country.getCapital().toLowerCase().equals(new StringBuilder(country.getCapital()).reverse().toString().toLowerCase()));
    }

    /**
     * Returns the country name with the most number of {@code 'e'} characters ignoring case.
     */
    public Optional<String> streamPipeline7() {
        return countries.stream().map(country -> country.getName()).max(Comparator.comparing(name->charCount(name,'e')));
    }

    /**
     *  Returns the capital with the most number of English vowels (i.e., {@code 'a'}, {@code 'e'}, {@code 'i'}, {@code 'o'}, {@code 'u'}).
     */
    public Optional<String> streamPipeline8() {
        return countries.stream().map(country -> country.getCapital()).max(Comparator.comparing(capital->vowelCount(capital)));
    }

    /**
     * Returns a map that contains for each character the number of occurrences in country names ignoring case.
     */
    public Map<Character, Long> streamPipeline9() {
        return countries.stream().flatMapToInt(country ->country.getName().toLowerCase().chars()).mapToObj(codePoint->(char)codePoint).collect(Collectors.groupingBy(Function.identity(),counting()));
    }

    /**
     * Returns a map that contains the number of countries for each possible timezone.
     */
    public Map<ZoneId, Long> streamPipeline10() {
        return countries.stream().flatMap(country ->country.getTimezones().stream()).collect(Collectors.groupingBy(Function.identity(),counting()));
    }

    /**
     * Returns the number of country names by region that starts with their two-letter country code ignoring case.
     */
    public Map<Region, Long> streamPipeline11() {
        return countries.stream().filter(country ->country.getName().substring(0,2).toUpperCase().equals(country.getCode())).collect(Collectors.groupingBy(Country::getRegion,counting()));
    }

    /**
     * Returns a map that contains the number of countries whose population is greater or equal than the population average versus the the number of number of countries with population below the average.
     */
    public Map<Boolean, Long> streamPipeline12() {
        return countries.stream().collect(partitioningBy(country -> country.getPopulation()>=countries.stream().mapToLong(Country::getPopulation).average().orElse(0),counting()));
    }

    /**
     * Returns a map that contains for each country code the name of the corresponding country in Portuguese ({@code "pt"}).
     */
    public Map<String, String> streamPipeline13() {
        return countries.stream().collect(Collectors.toMap(Country::getCode,country -> country.getTranslations().get("pt")));
    }

    /**
     * Returns the list of capitals by region whose name is the same is the same as the name of their country.
     */
    public Map<Region, List<String>> streamPipeline14() {
        return countries.stream().collect(groupingBy(Country::getRegion,filtering(country -> country.getCapital().equals(country.getName()),Collectors.mapping(Country::getCapital,Collectors.toList()))));
    }

    /**
     *  Returns a map of country name-population density pairs.
     */
    public Map<String, Double> streamPipeline15() {
        return countries.stream().collect(Collectors.toMap(Country::getName,country->country.getArea()!=null?country.getPopulation()/country.getArea().doubleValue():Double.NaN));
    }

}
