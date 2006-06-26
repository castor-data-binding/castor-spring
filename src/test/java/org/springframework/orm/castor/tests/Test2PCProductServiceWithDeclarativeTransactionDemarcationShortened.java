package org.springframework.orm.castor.tests;


import org.exolab.castor.service.ProductService;

/**
 * JUnit test case for Castor's Spring integration.
 * @author Werner Guttmann  
 */
public class Test2PCProductServiceWithDeclarativeTransactionDemarcationShortened 
    extends BaseSpringTestCaseWithTransactionDemarcation {

    protected void setUp() throws Exception {
        super.setUp();
        this.productService = (ProductService) this.context.getBean ("my2PCProductServiceDeclarativeShortened");
        assertNotNull (this.productService);
    }
}
