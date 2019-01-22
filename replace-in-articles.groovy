import com.liferay.portal.kernel.dao.orm.QueryUtil
import com.liferay.journal.service.JournalArticleLocalServiceUtil
import com.liferay.portal.kernel.util.PortalUtil
import org.slf4j.LoggerFactory

LOGGER = LoggerFactory.getLogger("${this.getClass().getName()} - Replace in articles")

previewMode = true

PATTERN = '/group/guest'
NEW_VALUE = '/guest'
LOCALES = ['nl_BE', 'fr_FR']
GROUP_ID = 10180

CONTEXT_SIZE = 20

List articles = JournalArticleLocalServiceUtil.getArticles(GROUP_ID)

LOCALES.each { locale ->
	articles.stream()
		.map {
			['article': it, 'content': it.getContentByLocale(locale)]
		}
		.filter {
			it.content.with { contains(locale) && contains(PATTERN) }
		}.each {
			trace("Updating article ${it.article.getArticleId()} (${locale})")
			trace("Before: <pre><xmp>${onlyContext(it.content)}</xmp></pre>")
			trace("After:  <pre><xmp>${onlyContext(it.content).replace(PATTERN, NEW_VALUE)}</xmp></pre>")

			if (!previewMode) {
				JournalArticleLocalServiceUtil.updateContent(
					it.article.getGroupId(), it.article.getArticleId(),
					it.article.getVersion(), it.content.replace(PATTERN, NEW_VALUE)
				)
			}
		}
}

def onlyContext(String html) {
	def position = html.indexOf(PATTERN)
	html.substring((position > CONTEXT_SIZE) ? (position - CONTEXT_SIZE) : 0, (position + PATTERN.length() + CONTEXT_SIZE))
}

def trace(String message) {
	println(message)
	LOGGER.info(removeXMLTags(message))
}

def removeXMLTags(String str) {
	str.minus("<pre><xmp>").minus("</xmp></pre>")
}
