package funding.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for loading CSV configuration data files as specified in the
 * Victorian ECE funding rules brief.
 */
public class CsvConfigLoader {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String CONFIG_BASE_PATH = "config/";

    /**
     * Load age eligibility configuration
     * 
     * @param fundingYear the funding year
     * @param scope       the scope (provider, LGA, statewide)
     * @param lgaCode     the LGA code (optional, for LGA scope)
     * @param providerId  the provider ID (optional, for provider scope)
     * @return Map of program type to cutoff date
     */
    public static Map<String, LocalDate> loadAgeEligibility(int fundingYear, String scope, String lgaCode,
            String providerId) {
        String filePath = CONFIG_BASE_PATH + "AGE_ELIGIBILITY.csv";
        Map<String, LocalDate> result = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length < 5)
                    continue;

                int year = Integer.parseInt(values[0].trim());
                String recordScope = values[1].trim();
                String recordLgaOrProviderId = values[2].trim();
                String programType = values[3].trim();
                LocalDate cutoffDate = LocalDate.parse(values[4].trim(), DATE_FORMAT);

                if (year == fundingYear) {
                    if (("statewide".equals(scope) && "statewide".equals(recordScope))
                            || ("lga".equals(scope) && "lga".equals(recordScope) && lgaCode.equals(recordLgaOrProviderId))
                            || ("provider".equals(scope) && "provider".equals(recordScope)
                                    && providerId.equals(recordLgaOrProviderId))) {
                        result.put(programType, cutoffDate);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Load Pre-Prep hours configuration
     * 
     * @param fundingYear the funding year
     * @param lgaCode     the LGA code
     * @param cohortKey   the cohort key
     * @return Pre-Prep hours per week
     */
    public static Double loadPrepHours(int fundingYear, String lgaCode, String cohortKey) {
        String filePath = CONFIG_BASE_PATH + "PREP_HOURS.csv";
        Double result = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length < 4)
                    continue;

                int year = Integer.parseInt(values[0].trim());
                String recordLga = values[1].trim();
                String recordCohort = values[2].trim();
                double hours = Double.parseDouble(values[3].trim());

                if (year == fundingYear && lgaCode.equals(recordLga) && cohortKey.equals(recordCohort)) {
                    result = hours;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Load allocation priority configuration
     * 
     * @param priorityCode the priority code
     * @return the allocation rank (lower is higher priority)
     */
    public static Integer loadAllocationPriority(String priorityCode) {
        String filePath = CONFIG_BASE_PATH + "ALLOCATION_PRIORITY.csv";
        Integer result = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length < 2)
                    continue;

                String code = values[0].trim();
                int rank = Integer.parseInt(values[1].trim());

                if (priorityCode.equals(code)) {
                    result = rank;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Load funding rates configuration
     * 
     * @param fundingYear    the funding year
     * @param programType    the program type
     * @param deliverySetting the delivery setting
     * @return Map of rate component code to rate value
     */
    public static Map<String, Double> loadRates(int fundingYear, String programType, String deliverySetting) {
        String filePath = CONFIG_BASE_PATH + "RATES.csv";
        Map<String, Double> result = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length < 5)
                    continue;

                int year = Integer.parseInt(values[0].trim());
                String recordProgramType = values[1].trim();
                String recordDeliverySetting = values[2].trim();
                String componentCode = values[3].trim();
                double rate = Double.parseDouble(values[4].trim());

                if (year == fundingYear && programType.equals(recordProgramType)
                        && deliverySetting.equals(recordDeliverySetting)) {
                    result.put(componentCode, rate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Check if a concession code is valid for KFS
     * 
     * @param concessionCode the concession code
     * @param effectiveDate  the date to check validity for
     * @return true if the concession is valid for KFS
     */
    public static boolean isKfsConcessionValid(String concessionCode, LocalDate effectiveDate) {
        String filePath = CONFIG_BASE_PATH + "KFS_CONCESSIONS.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length < 4)
                    continue;

                String code = values[0].trim();
                boolean isEligible = Boolean.parseBoolean(values[1].trim());
                LocalDate startDate = LocalDate.parse(values[2].trim(), DATE_FORMAT);
                LocalDate endDate = null;
                if (!values[3].trim().isEmpty()) {
                    endDate = LocalDate.parse(values[3].trim(), DATE_FORMAT);
                }

                if (concessionCode.equals(code) && isEligible
                        && (effectiveDate.isEqual(startDate) || effectiveDate.isAfter(startDate))
                        && (endDate == null || effectiveDate.isBefore(endDate) || effectiveDate.isEqual(endDate))) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Load inclusion support components configuration
     * 
     * @param fundingYear the funding year
     * @return List of inclusion support components
     */
    public static List<InclusionComponent> loadInclusionComponents(int fundingYear) {
        String filePath = CONFIG_BASE_PATH + "INCLUSION_MENU.csv";
        List<InclusionComponent> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean header = true;

            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length < 4)
                    continue;

                int year = Integer.parseInt(values[0].trim());
                String componentCode = values[1].trim();
                String componentName = values[2].trim();
                double rate = Double.parseDouble(values[3].trim());

                if (year == fundingYear) {
                    result.add(new InclusionComponent(componentCode, componentName, rate));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Check if CSV config files exist
     * 
     * @return true if all required CSV files exist
     */
    public static boolean configFilesExist() {
        String[] requiredFiles = { "AGE_ELIGIBILITY.csv", "PREP_HOURS.csv", "ALLOCATION_PRIORITY.csv", "RATES.csv",
                "KFS_CONCESSIONS.csv", "INCLUSION_MENU.csv", "VEYLDF_CODES.csv" };

        for (String file : requiredFiles) {
            Path path = Paths.get(CONFIG_BASE_PATH + file);
            if (!Files.exists(path)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Inner class representing an inclusion component
     */
    public static class InclusionComponent {
        private String code;
        private String name;
        private double rate;

        public InclusionComponent(String code, String name, double rate) {
            this.code = code;
            this.name = name;
            this.rate = rate;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public double getRate() {
            return rate;
        }
    }
}
