public class DpopAuthenticationFilter
    implements Filter {

    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException,
                   ServletException {

        HttpServletRequest req =
            (HttpServletRequest) request;

        String dpop =
            req.getHeader("DPoP");

        String auth =
            req.getHeader("Authorization");

        System.out.println(
            "Authorization = " + auth);

        System.out.println(
            "DPoP = " + dpop);

        chain.doFilter(
            request,
            response);
    }
}
