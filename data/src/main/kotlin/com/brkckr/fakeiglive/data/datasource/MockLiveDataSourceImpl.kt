package com.brkckr.fakeiglive.data.datasource

import com.brkckr.fakeiglive.domain.model.Comment
import javax.inject.Inject
import javax.inject.Singleton

// provide static mock data and timing configuration
@Singleton
class MockLiveDataSourceImpl @Inject constructor() : LiveDataSource {

    override fun getComments(): List<Comment> =
        COMMENTS // returns pre-defined list of mock messages

    override fun getViewerRange(): IntRange = 10_000..100_000 // min and max simulated viewers

    override fun getViewerChangeRange(): IntRange = 150..1_500 // variance for viewer count updates

    override fun getTrendChangeProbability(): Float =
        0.35f // chance of reversing viewer count direction

    override fun getCommentDelayRange(): LongRange =
        2_000L..3_000L // interval between new mock comments

    override fun getViewerDelayRange(): LongRange =
        1_200L..2_400L // interval for updating viewer stats

    override fun getHeartDelayRange(): LongRange =
        450L..1_100L // interval for automatic heart bursts

    private companion object {
        val COMMENTS = listOf(
            comment("evanpetes", "Classy and sassy \uD83D\uDE0E", 32),
            comment("emmaroberts", "Absolutely glowing \uD83E\uDD70", 47),
            comment("ross_lynch", "wow-wow \uD83E\uDD29", 12),
            comment("cynthialovely", "That's so fun!", 44),
            comment("maddy.sun", "You look amazing!", 5),
            comment("leo.wav", "Greetings from Istanbul \uD83D\uDC4B", 11),
            comment("sophia.rose", "This live made my day \u2764\uFE0F", 25),
            comment("noahframes", "Camera quality is so good", 14),
            comment("miaonair", "Say hi to us!", 9),
            comment("alex.jpg", "Iconic \u2728", 53),
            comment("deniz.online", "T\u00FCrkiye'den selamlar! \uD83C\uDDF9\uD83C\uDDF7", 36),
            comment("melis.daily", "Enerjin harika \uD83D\uDD25", 45),
            comment("berk.works", "Bu yay\u0131n hi\u00E7 bitmesin", 17),
            comment("selin.jpg", "Sa\u00E7lar\u0131na bay\u0131ld\u0131m \uD83D\uDE0D", 49),
            comment("arda.music", "Arkada hangi \u015Fark\u0131 \u00E7al\u0131yor?", 15),
            comment("eceonair", "Kalpler benden \u2764\uFE0F\u2764\uFE0F\u2764\uFE0F", 41),
            comment("lucas.mov", "The lighting is perfect", 3),
            comment("oliviahere", "Where did you get that outfit?", 10),
            comment("jamie.vibes", "Main character energy \u2728", 8),
            comment("zoe.camera", "This angle is everything", 16),
            comment("ethanlive", "We need a room tour!", 13),
            comment("ava.nights", "I joined at the perfect time", 20),
            comment("liamcreates", "Instant mood boost", 22),
            comment("graceful", "Your smile is contagious \uD83D\uDE0A", 24),
            comment("theo.wave", "Sending love from London", 18),
            comment("nina.notes", "Best notification today", 27),
            comment("kai.studio", "That color suits you so well", 29),
            comment("lilymoon", "Can we get a makeup tutorial?", 31),
            comment("samuel.jpg", "The vibes are immaculate", 33),
            comment("chloetalks", "I was waiting for this live!", 35),
            comment("mateo.fm", "Hola from Madrid \uD83D\uDC4B", 38),
            comment("camille.paris", "Bonjour! Trop belle \u2728", 39),
            comment("giulia.roma", "Ciao from Rome!", 42),
            comment("hana.seoul", "Love this look so much", 43),
            comment("yuki.day", "Konnichiwa! So cute \uD83C\uDF38", 46),
            comment("lara.berlin", "Greetings from Berlin", 48),
            comment("isla.skies", "Your lives are always the best", 50),
            comment("max.edit", "Screenshot worthy \uD83D\uDCF8", 51),
            comment("rubyretro", "This feels like a movie scene", 52),
            comment("owen.chat", "What are we doing today?", 54),
            comment("maya.flow", "You just made everyone smile", 55),
            comment("finn.focus", "The front camera looks amazing", 56),
            comment("ella.sunset", "Golden hour energy \u2600\uFE0F", 57),
            comment("dylan.loop", "One more song please!", 58),
            comment("iris.colors", "Obsessed with this whole vibe", 59),
            comment("milo.echo", "First time catching the live!", 60),
            comment("layla.lens", "Drop the skincare routine \uD83D\uDE4F", 1),
            comment("asher.wave", "Everyone tap the hearts!", 2),
        )

        fun comment(username: String, text: String, imageId: Int) = Comment(
            username = username,
            text = text,
            profileImageUri = "https://i.pravatar.cc/160?img=$imageId",
        )
    }
}
