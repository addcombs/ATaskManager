package com.ataskmanager.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**  States Class for all US states
 * @author Adam Combs
 * @author Seth Wampole
 */
public class States {

          private static final Map<String, String> STATE_MAP;
          private static final List<String> STATE_LIST = new ArrayList<>();

          static{
                    STATE_LIST.add("AK");
                    STATE_LIST.add("AL");
                    STATE_LIST.add("AR");
                    STATE_LIST.add("AZ");
                    STATE_LIST.add("CA");
                    STATE_LIST.add("CO");
                    STATE_LIST.add("CT");
                    STATE_LIST.add("DC");
                    STATE_LIST.add("DE");
                    STATE_LIST.add("FL");
                    STATE_LIST.add("GA");
                    STATE_LIST.add("GU");
                    STATE_LIST.add("HI");
                    STATE_LIST.add("IA");
                    STATE_LIST.add("ID");
                    STATE_LIST.add("IL");
                    STATE_LIST.add("IN");
                    STATE_LIST.add("KS");
                    STATE_LIST.add("KY");
                    STATE_LIST.add("LA");
                    STATE_LIST.add("MA");
                    STATE_LIST.add("MD");
                    STATE_LIST.add("ME");
                    STATE_LIST.add("MI");
                    STATE_LIST.add("MN");
                    STATE_LIST.add("MO");
                    STATE_LIST.add("MS");
                    STATE_LIST.add("MT");
                    STATE_LIST.add("NC");
                    STATE_LIST.add("ND");
                    STATE_LIST.add("NE");
                    STATE_LIST.add("NH");
                    STATE_LIST.add("NJ");
                    STATE_LIST.add("NM");
                    STATE_LIST.add("NV");
                    STATE_LIST.add("NY");
                    STATE_LIST.add("OH");
                    STATE_LIST.add("OK");
                    STATE_LIST.add("OR");
                    STATE_LIST.add("PA");
                    STATE_LIST.add("PR");
                    STATE_LIST.add("RI");
                    STATE_LIST.add("SC");
                    STATE_LIST.add("SD");
                    STATE_LIST.add("TN");
                    STATE_LIST.add("TX");
                    STATE_LIST.add("UT");
                    STATE_LIST.add("VA");
                    STATE_LIST.add("VI");
                    STATE_LIST.add("VT");
                    STATE_LIST.add("WA");
                    STATE_LIST.add("WI");
                    STATE_LIST.add("WV");
                    STATE_LIST.add( "WY");
          }

          static {
                    STATE_MAP = new HashMap<>();
                    STATE_MAP.put("AL", "Alabama");
                    STATE_MAP.put("AK", "Alaska");
                    STATE_MAP.put("AZ", "Arizona");
                    STATE_MAP.put("AR", "Arkansas");
                    STATE_MAP.put("CA", "California");
                    STATE_MAP.put("CO", "Colorado");
                    STATE_MAP.put("CT", "Connecticut");
                    STATE_MAP.put("DC", "District Of Columbia");
                    STATE_MAP.put("DE", "Delaware");
                    STATE_MAP.put("FL", "Florida");
                    STATE_MAP.put("GA", "Georgia");
                    STATE_MAP.put("GU", "Guam");
                    STATE_MAP.put("HI", "Hawaii");
                    STATE_MAP.put("ID", "Idaho");
                    STATE_MAP.put("IL", "Illinois");
                    STATE_MAP.put("IN", "Indiana");
                    STATE_MAP.put("IA", "Iowa");
                    STATE_MAP.put("KS", "Kansas");
                    STATE_MAP.put("KY", "Kentucky");
                    STATE_MAP.put("LA", "Louisiana");
                    STATE_MAP.put("ME", "Maine");
                    STATE_MAP.put("MD", "Maryland");
                    STATE_MAP.put("MA", "Massachusetts");
                    STATE_MAP.put("MI", "Michigan");
                    STATE_MAP.put("MN", "Minnesota");
                    STATE_MAP.put("MS", "Mississippi");
                    STATE_MAP.put("MO", "Missouri");
                    STATE_MAP.put("MT", "Montana");
                    STATE_MAP.put("NE", "Nebraska");
                    STATE_MAP.put("NV", "Nevada");
                    STATE_MAP.put("NH", "New Hampshire");
                    STATE_MAP.put("NJ", "New Jersey");
                    STATE_MAP.put("NM", "New Mexico");
                    STATE_MAP.put("NY", "New York");
                    STATE_MAP.put("NC", "North Carolina");
                    STATE_MAP.put("ND", "North Dakota");
                    STATE_MAP.put("OH", "Ohio");
                    STATE_MAP.put("OK", "Oklahoma");
                    STATE_MAP.put("OR", "Oregon");
                    STATE_MAP.put("PA", "Pennsylvania");
                    STATE_MAP.put("PR", "Puerto Rico");
                    STATE_MAP.put("RI", "Rhode Island");
                    STATE_MAP.put("SC", "South Carolina");
                    STATE_MAP.put("SD", "South Dakota");
                    STATE_MAP.put("TN", "Tennessee");
                    STATE_MAP.put("TX", "Texas");
                    STATE_MAP.put("UT", "Utah");
                    STATE_MAP.put("VT", "Vermont");
                    STATE_MAP.put("VI", "Virgin Islands");
                    STATE_MAP.put("VA", "Virginia");
                    STATE_MAP.put("WA", "Washington");
                    STATE_MAP.put("WV", "West Virginia");
                    STATE_MAP.put("WI", "Wisconsin");
                    STATE_MAP.put("WY", "Wyoming");
          }

          public static Map<String, String> getStateMap() {
                    return STATE_MAP;
          }

          public static List<String> getStateList() {
                    return STATE_LIST;
          }

}

