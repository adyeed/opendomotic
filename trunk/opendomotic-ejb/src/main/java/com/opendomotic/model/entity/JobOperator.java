/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.model.entity;

/**
 *
 * @author jaques
 */
public enum JobOperator {
    
    EQUAL {
        @Override
        public String toString() {
            return "=";
        }
    },
    
    DIFERENT {
        @Override
        public String toString() {
            return "<>";
        }
    },
    
    GREATHER {
        @Override
        public String toString() {
            return ">";
        }
    },
    
    GREATHER_EQUAL {
        @Override
        public String toString() {
            return ">=";
        }
    },
    
    LESS {
        @Override
        public String toString() {
            return "<";
        }
    },
    
    LESS_EQUAL {
        @Override
        public String toString() {
            return "<=";
        }
    }
    
}
