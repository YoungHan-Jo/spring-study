/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/9.0.50
 * Generated at: 2021-10-21 05:30:53 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.views.member;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class join_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    if (!javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSP?????? ?????? GET, POST ?????? HEAD ??????????????? ???????????????. Jasper??? OPTIONS ????????? ?????? ???????????????.");
        return;
      }
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html lang=\"ko\">\r\n");
      out.write("\r\n");
      out.write("<head>\r\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/WEB-INF/views/include/head.jsp", out, false);
      out.write("\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body>\r\n");
      out.write("\r\n");
      out.write("	<!-- Navbar area -->\r\n");
      out.write("	");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/WEB-INF/views/include/top.jsp", out, false);
      out.write("\r\n");
      out.write("	<!-- end of Navbar area -->\r\n");
      out.write("\r\n");
      out.write("	<!-- Page Layout here -->\r\n");
      out.write("	<div class=\"row container\">\r\n");
      out.write("\r\n");
      out.write("		<!-- left menu area -->\r\n");
      out.write("		");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/WEB-INF/views/include/left.jsp", out, false);
      out.write("\r\n");
      out.write("		<!-- end of left menu area -->\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("		<div class=\"col s12 m8 l9\">\r\n");
      out.write("			<!-- Teal page content  -->\r\n");
      out.write("			<div class=\"section\">\r\n");
      out.write("\r\n");
      out.write("				<!-- card panel -->\r\n");
      out.write("				<div class=\"card-panel\">\r\n");
      out.write("					<div class=\"row\">\r\n");
      out.write("						<div class=\"col s12\" style=\"padding: 0 5%;\">\r\n");
      out.write("\r\n");
      out.write("							<h5>????????????</h5>\r\n");
      out.write("							<div class=\"divider\" style=\"margin: 30px 0;\"></div>\r\n");
      out.write("\r\n");
      out.write("							<!-- POST???????????? ????????? ????????? ????????? ????????? -> ?????? url??? ?????????. -->\r\n");
      out.write("							<form action=\"/member/join\" method=\"POST\" id=\"frm\"\r\n");
      out.write("								name=\"frm\">\r\n");
      out.write("								<div class=\"row\">\r\n");
      out.write("									<div class=\"input-field col s12 m9\">\r\n");
      out.write("										<i class=\"material-icons prefix\">account_box</i> \r\n");
      out.write("										<input id=\"id\" type=\"text\" name='id' data-length=\"20\"> \r\n");
      out.write("										<label for=\"id\">?????????</label>\r\n");
      out.write("										<span class=\"helper-text\"></span>\r\n");
      out.write("									</div>\r\n");
      out.write("\r\n");
      out.write("									<div class=\"col s12 m3\">\r\n");
      out.write("										<button type=\"button\"\r\n");
      out.write("											class=\"waves-effect waves-light btn-small\" id=\"btnIdDupChk\">ID????????????</button>\r\n");
      out.write("									</div>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("								</div>\r\n");
      out.write("\r\n");
      out.write("								<div class=\"row\">\r\n");
      out.write("									<div class=\"input-field col s12\">\r\n");
      out.write("										<i class=\"material-icons prefix\">lock</i> <input id=\"passwd\"\r\n");
      out.write("											type=\"password\" name=\"passwd\" class=\"validate\"\r\n");
      out.write("											data-length=\"10\"> <label for=\"passwd\">????????????</label> <span\r\n");
      out.write("											class=\"helper-text\" data-error=\"??????????????? 10??????????????? ???????????????.\"\r\n");
      out.write("											data-success=\"OK!\">Helper text</span>\r\n");
      out.write("									</div>\r\n");
      out.write("\r\n");
      out.write("									<div class=\"input-field col s12\">\r\n");
      out.write("										<i class=\"material-icons prefix\">check</i> <input id=\"passwd2\"\r\n");
      out.write("											type=\"password\" data-length=\"10\"> <label\r\n");
      out.write("											for=\"passwd2\">???????????? ?????????</label> <span class=\"helper-text\"\r\n");
      out.write("											data-error=\"\" data-success=\"\"></span>\r\n");
      out.write("									</div>\r\n");
      out.write("								</div>\r\n");
      out.write("\r\n");
      out.write("								<div class=\"row\">\r\n");
      out.write("									<div class=\"input-field col s12\">\r\n");
      out.write("										<i class=\"material-icons prefix\">person</i> <input id=\"name\"\r\n");
      out.write("											type=\"text\" name=\"name\" class=\"validate\"> <label\r\n");
      out.write("											for=\"name\">??????</label>\r\n");
      out.write("									</div>\r\n");
      out.write("								</div>\r\n");
      out.write("\r\n");
      out.write("								<div class=\"row\">\r\n");
      out.write("									<div class=\"input-field\">\r\n");
      out.write("										<i class=\"material-icons prefix\">event</i> <input type=\"date\"\r\n");
      out.write("											id=\"birthday\" name=\"birthday\"> <label for=\"birthday\">????????????</label>\r\n");
      out.write("									</div>\r\n");
      out.write("								</div>\r\n");
      out.write("\r\n");
      out.write("								<div class=\"row\">\r\n");
      out.write("									<div class=\"input-field\">\r\n");
      out.write("										<i class=\"material-icons prefix\">wc</i> <select name=\"gender\">\r\n");
      out.write("											<option value=\"\" disabled selected>????????? ???????????????.</option>\r\n");
      out.write("											<option value=\"M\">??????</option>\r\n");
      out.write("											<option value=\"F\">??????</option>\r\n");
      out.write("											<option value=\"N\">?????? ??????</option>\r\n");
      out.write("										</select> <label>??????</label>\r\n");
      out.write("									</div>\r\n");
      out.write("								</div>\r\n");
      out.write("\r\n");
      out.write("								<div class=\"row\">\r\n");
      out.write("									<div class=\"input-field col s12\">\r\n");
      out.write("										<i class=\"material-icons prefix\">mail</i> <input id=\"email\"\r\n");
      out.write("											type=\"email\" name=\"email\" class=\"validate\"> <label\r\n");
      out.write("											for=\"email\">?????? ?????? ?????????</label>\r\n");
      out.write("									</div>\r\n");
      out.write("								</div>\r\n");
      out.write("\r\n");
      out.write("								<p class=\"row center\">\r\n");
      out.write("									?????? ????????? ?????? : &nbsp;&nbsp; <label> <input\r\n");
      out.write("										name=\"recvEmail\" value=\"Y\" type=\"radio\" checked /> <span>???</span>\r\n");
      out.write("									</label> &nbsp;&nbsp; <label> <input name=\"recvEmail\" value=\"N\"\r\n");
      out.write("										type=\"radio\" /> <span>?????????</span>\r\n");
      out.write("									</label>\r\n");
      out.write("								</p>\r\n");
      out.write("\r\n");
      out.write("								<br>\r\n");
      out.write("								<div class=\"row center\">\r\n");
      out.write("									<button class=\"btn waves-effect waves-light\" type=\"submit\">\r\n");
      out.write("										???????????? <i class=\"material-icons right\">create</i>\r\n");
      out.write("									</button>\r\n");
      out.write("									&nbsp;&nbsp;\r\n");
      out.write("									<button class=\"btn waves-effect waves-light\" type=\"reset\">\r\n");
      out.write("										????????? <i class=\"material-icons right\">clear</i>\r\n");
      out.write("									</button>\r\n");
      out.write("								</div>\r\n");
      out.write("							</form>\r\n");
      out.write("\r\n");
      out.write("						</div>\r\n");
      out.write("					</div>\r\n");
      out.write("				</div>\r\n");
      out.write("				<!-- end of card panel -->\r\n");
      out.write("\r\n");
      out.write("			</div>\r\n");
      out.write("		</div>\r\n");
      out.write("\r\n");
      out.write("	</div>\r\n");
      out.write("\r\n");
      out.write("	<!-- footer area -->\r\n");
      out.write("	");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/WEB-INF/views/include/bottom.jsp", out, false);
      out.write("\r\n");
      out.write("	<!-- end of footer area -->\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("	<!--  Scripts-->\r\n");
      out.write("	");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/WEB-INF/views/include/commonJs.jsp", out, false);
      out.write("\r\n");
      out.write("	<script>\r\n");
      out.write("		// ?????? ?????? ?????? ????????? ?????? ?????????\r\n");
      out.write("	    $('input#id, input#passwd, input#passwd2').characterCounter();\r\n");
      out.write("	    \r\n");
      out.write("		//????????? ???????????? ??????\r\n");
      out.write("		$('#btnIdDupChk').on('click',function(){\r\n");
      out.write("			const id = $('#id').val();\r\n");
      out.write("			\r\n");
      out.write("			//id??? ???????????? '????????????????????????' ????????? ??????\r\n");
      out.write("			if(id == ''){\r\n");
      out.write("				alert('???????????? ???????????????.');\r\n");
      out.write("				$('#id').focus();\r\n");
      out.write("				return;\r\n");
      out.write("			}\r\n");
      out.write("			\r\n");
      out.write("			// id ???????????? ????????? ??????\r\n");
      out.write("			open('/member/joinIdDupChk?id='+id,'idDupChk','width=500, height=400');\r\n");
      out.write("			//open(??????, ??? ??????, ????????????)\r\n");
      out.write("		})\r\n");
      out.write("		\r\n");
      out.write("		$('input#id').on('focusout',function(){\r\n");
      out.write("			var id = $(this).val();\r\n");
      out.write("			if (id.length == 0){\r\n");
      out.write("				return;\r\n");
      out.write("			}\r\n");
      out.write("\r\n");
      out.write("			var $inputId = $(this);\r\n");
      out.write("			var $span = $inputId.next().next();\r\n");
      out.write("			\r\n");
      out.write("			$.ajax({\r\n");
      out.write("				url : '/api/members/' + id,\r\n");
      out.write("				method : 'GET',\r\n");
      out.write("				success : function(data){\r\n");
      out.write("					console.log(data);\r\n");
      out.write("					console.log(typeof data);\r\n");
      out.write("										\r\n");
      out.write("					if(data.count == 0){ // ??????????????? ?????????\r\n");
      out.write("						$span.html('??????????????? ????????? ?????????.').css('color','green');\r\n");
      out.write("					}else{ // data.count == 1 // ????????? ??????\r\n");
      out.write("						$span.html('???????????? ????????? ?????????.').css('color','red');\r\n");
      out.write("					}\r\n");
      out.write("				}// success\r\n");
      out.write("			});\r\n");
      out.write("		})\r\n");
      out.write("		\r\n");
      out.write("		\r\n");
      out.write("		\r\n");
      out.write("		$('#passwd2').on('focusout',function(){\r\n");
      out.write("			const passwd = $('#passwd').val();\r\n");
      out.write("			const passwd2 = $(this).val();\r\n");
      out.write("			\r\n");
      out.write("			if(passwd == passwd2){\r\n");
      out.write("				var $span = $(this).closest('div.input-field').find('span.helper-text');\r\n");
      out.write("				$span.html('???????????? ?????????').css('color','green');\r\n");
      out.write("				$(this).removeClass('invalid').addClass('valid');\r\n");
      out.write("			}else{\r\n");
      out.write("				var $span = $(this).closest('div.input-field').find('span.helper-text');\r\n");
      out.write("				$span.html('???????????? ?????????????????? ??????').css('color','red');\r\n");
      out.write("				$(this).removeClass('valid').addClass('invalid');\r\n");
      out.write("			}\r\n");
      out.write("			\r\n");
      out.write("		})\r\n");
      out.write("	    \r\n");
      out.write("	\r\n");
      out.write("	\r\n");
      out.write("		$('form#frm').on('submit',function(event){\r\n");
      out.write("			\r\n");
      out.write("			var id = $('#id').val();\r\n");
      out.write("			if(id.length < 3 || id.length >13){\r\n");
      out.write("				event.preventDefault();\r\n");
      out.write("				\r\n");
      out.write("				alert('???????????? 3?????? ?????? 13?????? ????????? ???????????????.');\r\n");
      out.write("				\r\n");
      out.write("				$('#id').select();\r\n");
      out.write("				\r\n");
      out.write("				return;\r\n");
      out.write("			}\r\n");
      out.write("			\r\n");
      out.write("		})	\r\n");
      out.write("	</script>\r\n");
      out.write("</body>\r\n");
      out.write("\r\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
